const reshoots = {
    async loadReshoots() {
        const turbineId = document.getElementById('reshoot-filter-turbine')?.value || '';
        const statusFilter = document.getElementById('reshoot-filter-status')?.value || '';

        try {
            const res = await api.reshoots.list(
                null,
                turbineId ? Number(turbineId) : null
            );
            let reshoots = res.data || [];

            if (statusFilter === 'pending') {
                reshoots = reshoots.filter(r => !r.isCompleted);
            } else if (statusFilter === 'completed') {
                reshoots = reshoots.filter(r => r.isCompleted);
            }

            const container = document.getElementById('reshoot-list');
            if (reshoots.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无复拍记录</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>复拍编号</th>
                            <th>缺陷ID</th>
                            <th>机组ID</th>
                            <th>第几次</th>
                            <th>状态</th>
                            <th>计划时间</th>
                            <th>实际时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${reshoots.map(r => `
                            <tr>
                                <td>${helpers.escapeHtml(r.reshootCode || '-')}</td>
                                <td>${r.defectId || '-'}</td>
                                <td>${r.turbineId || '-'}</td>
                                <td>${r.reshootOrder || 1}</td>
                                <td><span class="status-tag ${r.isCompleted ? 'status-success' : 'status-warning'}">${r.isCompleted ? '已完成' : '待完成'}</span></td>
                                <td>${helpers.formatDateTime(r.scheduledTime)}</td>
                                <td>${helpers.formatDateTime(r.actualTime)}</td>
                                <td>
                                    ${!r.isCompleted ? `<button class="btn-link" onclick="reshoots.completeReshoot(${r.id})">完成</button>` : ''}
                                    <button class="btn-link" onclick="reshoots.showDetail(${r.id})">详情</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载复拍列表失败: ' + error.message, 'error');
        }
    },

    async showDetail(reshootId) {
        try {
            const res = await api.reshoots.get(reshootId);
            const r = res.data;

            let photosHtml = '';
            if (r.photoUrls) {
                const photos = r.photoUrls.split(',').filter(Boolean);
                photosHtml = `<div class="photo-gallery">
                    ${photos.map(() => `<div class="photo-thumb">🖼️</div>`).join('')}
                </div>`;
            }

            const content = `
                <div class="detail-section">
                    <h4>基本信息</h4>
                    <div class="detail-row"><span class="detail-label">复拍编号:</span><span class="detail-value">${helpers.escapeHtml(r.reshootCode || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">缺陷ID:</span><span class="detail-value">${r.defectId || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">机组ID:</span><span class="detail-value">${r.turbineId || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">复拍次数:</span><span class="detail-value">第${r.reshootOrder || 1}次</span></div>
                    <div class="detail-row"><span class="detail-label">状态:</span><span class="detail-value"><span class="status-tag ${r.isCompleted ? 'status-success' : 'status-warning'}">${r.isCompleted ? '已完成' : '待完成'}</span></span></div>
                </div>
                <div class="detail-section">
                    <h4>复拍原因</h4>
                    <p style="font-size: 13px; color: #595959;">${helpers.escapeHtml(r.reshootReason || '无')}</p>
                </div>
                <div class="detail-section">
                    <h4>计划信息</h4>
                    <div class="detail-row"><span class="detail-label">计划时间:</span><span class="detail-value">${helpers.formatDateTime(r.scheduledTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">计划风速:</span><span class="detail-value">${r.windSpeedScheduled || '-'} m/s</span></div>
                </div>
                ${r.isCompleted ? `
                <div class="detail-section">
                    <h4>执行信息</h4>
                    <div class="detail-row"><span class="detail-label">实际时间:</span><span class="detail-value">${helpers.formatDateTime(r.actualTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">实际风速:</span><span class="detail-value">${r.windSpeedActual || '-'} m/s</span></div>
                    <div class="detail-row"><span class="detail-label">执行人:</span><span class="detail-value">${helpers.escapeHtml(r.reshootOperator || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">复拍结果:</span><span class="detail-value">${helpers.escapeHtml(r.reshootResult || '-')}</span></div>
                    ${photosHtml}
                </div>
                ` : ''}
                ${r.remark ? `
                <div class="detail-section">
                    <h4>备注</h4>
                    <p style="font-size: 13px; color: #595959;">${helpers.escapeHtml(r.remark)}</p>
                </div>
                ` : ''}
            `;

            document.getElementById('defect-detail-content').innerHTML = content;
            document.getElementById('modal-title').textContent = '复拍详情';
            document.getElementById('defect-modal-actions').innerHTML = 
                '<button class="btn-default" onclick="helpers.closeModal(\'defect-modal\')">关闭</button>';
            helpers.showModal('defect-modal');
        } catch (error) {
            helpers.showToast('加载复拍详情失败: ' + error.message, 'error');
        }
    },

    async completeReshoot(reshootId) {
        const result = prompt('请输入复拍结果描述:');
        if (result === null) return;

        const windSpeed = prompt('请输入实际风速 (m/s):', '6.0');
        if (windSpeed === null) return;

        try {
            await api.reshoots.complete(reshootId, {
                reshootResult: result,
                windSpeedActual: windSpeed ? Number(windSpeed) : null,
                photoUrls: ''
            });
            helpers.showToast('复拍完成');
            this.loadReshoots();
            defects.loadDefects();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('完成复拍失败: ' + error.message, 'error');
        }
    }
};
