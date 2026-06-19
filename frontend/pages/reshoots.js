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
                            <th>叶片编号</th>
                            <th>要求角度</th>
                            <th>角度偏差</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${reshoots.map(r => `
                            <tr>
                                <td>${helpers.escapeHtml(r.reshootCode || '-')}</td>
                                <td>${r.defectId || '-'}</td>
                                <td>${helpers.escapeHtml(r.bladeNumber || '-')}</td>
                                <td>${r.requiredAngle ? r.requiredAngle + '°' : '-'}</td>
                                <td>${r.angleDeviation != null ? r.angleDeviation + '°' : '-'}</td>
                                <td>
                                    <span class="status-tag ${r.isCompleted ? 'status-success' : 'status-warning'}">${r.isCompleted ? '已完成' : '待完成'}</span>
                                    ${r.isCompleted && r.isAngleValid === false ? '<span class="status-tag status-error" style="margin-left: 4px;">角度不符</span>' : ''}
                                </td>
                                <td>
                                    ${!r.isCompleted ? `<button class="btn-link" onclick="reshoots.completeReshoot(${r.id})">完成</button>` : ''}
                                    ${r.isCompleted ? `<button class="btn-link" onclick="reshoots.showComparison(${r.id})">比对</button>` : ''}
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
                    <h4>叶片与角度信息</h4>
                    <div class="detail-row"><span class="detail-label">叶片编号:</span><span class="detail-value">${helpers.escapeHtml(r.bladeNumber || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">叶片位置:</span><span class="detail-value">${helpers.escapeHtml(r.bladePosition || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">要求拍摄角度:</span><span class="detail-value">${r.requiredAngle != null ? r.requiredAngle + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">要求方位角:</span><span class="detail-value">${r.requiredAzimuth != null ? r.requiredAzimuth + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">角度容差:</span><span class="detail-value">${r.angleTolerance != null ? r.angleTolerance + '°' : '5°'}</span></div>
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
                    <div class="detail-row"><span class="detail-label">实际拍摄角度:</span><span class="detail-value">${r.actualAngle != null ? r.actualAngle + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">实际方位角:</span><span class="detail-value">${r.actualAzimuth != null ? r.actualAzimuth + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">角度偏差:</span><span class="detail-value">${r.angleDeviation != null ? r.angleDeviation + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">角度是否有效:</span><span class="detail-value">
                        <span class="status-tag ${r.isAngleValid ? 'status-success' : 'status-error'}">${r.isAngleValid ? '是' : '否'}</span>
                    </span></div>
                    <div class="detail-row"><span class="detail-label">比对结果:</span><span class="detail-value">${helpers.escapeHtml(r.comparisonResult || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">比对意见:</span><span class="detail-value">${helpers.escapeHtml(r.comparisonOpinion || '-')}</span></div>
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

        const actualAngle = prompt('请输入实际拍摄角度 (°):', '0');
        if (actualAngle === null) return;

        const actualAzimuth = prompt('请输入实际方位角 (°):', '0');
        if (actualAzimuth === null) return;

        const comparisonResult = prompt('请输入比对结果 (确认缺陷/排除缺陷/需要进一步复核):', '确认缺陷');
        if (comparisonResult === null) return;

        const comparisonOpinion = prompt('请输入比对意见:');
        if (comparisonOpinion === null) return;

        try {
            await api.reshoots.complete(reshootId, {
                reshootResult: result,
                windSpeedActual: windSpeed ? Number(windSpeed) : null,
                actualAngle: actualAngle ? Number(actualAngle) : null,
                actualAzimuth: actualAzimuth ? Number(actualAzimuth) : null,
                comparisonResult: comparisonResult,
                comparisonOpinion: comparisonOpinion,
                photoUrls: ''
            });
            helpers.showToast('复拍完成');
            this.loadReshoots();
            defects.loadDefects();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('完成复拍失败: ' + error.message, 'error');
        }
    },

    async showComparison(reshootId) {
        try {
            const res = await api.reshoots.getComparison(reshootId);
            const data = res.data || {};
            const defect = data.defect || {};
            const reshoot = data.reshoot || {};

            const content = `
                <div class="detail-section">
                    <h4>复拍比对结果</h4>
                    <div class="detail-row"><span class="detail-label">要求拍摄角度:</span><span class="detail-value">${data.requiredAngle != null ? data.requiredAngle + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">实际拍摄角度:</span><span class="detail-value">${data.actualAngle != null ? data.actualAngle + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">角度偏差:</span><span class="detail-value">${data.angleDeviation != null ? data.angleDeviation + '°' : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">角度是否有效:</span><span class="detail-value">
                        <span class="status-tag ${data.isAngleValid ? 'status-success' : 'status-error'}">${data.isAngleValid ? '是' : '否'}</span>
                    </span></div>
                    <div class="detail-row"><span class="detail-label">比对结果:</span><span class="detail-value">${helpers.escapeHtml(data.comparisonResult || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">比对意见:</span><span class="detail-value">${helpers.escapeHtml(data.comparisonOpinion || '-')}</span></div>
                </div>
                <div class="detail-section">
                    <h4>照片比对</h4>
                    <div style="display: flex; gap: 20px;">
                        <div style="flex: 1;">
                            <div style="font-weight: 600; margin-bottom: 8px; color: #595959;">原始照片</div>
                            <div class="photo-gallery">
                                ${(data.originalPhotos || '').split(',').filter(Boolean).length > 0 
                                    ? (data.originalPhotos || '').split(',').filter(Boolean).map(() => `<div class="photo-thumb" style="width: 100%; height: 120px;">🖼️</div>`).join('')
                                    : '<div style="color: #999; font-size: 13px;">暂无照片</div>'
                                }
                            </div>
                        </div>
                        <div style="flex: 1;">
                            <div style="font-weight: 600; margin-bottom: 8px; color: #595959;">复拍照片</div>
                            <div class="photo-gallery">
                                ${(data.reshootPhotos || '').split(',').filter(Boolean).length > 0 
                                    ? (data.reshootPhotos || '').split(',').filter(Boolean).map(() => `<div class="photo-thumb" style="width: 100%; height: 120px;">🖼️</div>`).join('')
                                    : '<div style="color: #999; font-size: 13px;">暂无照片</div>'
                                }
                            </div>
                        </div>
                    </div>
                </div>
            `;

            document.getElementById('defect-detail-content').innerHTML = content;
            document.getElementById('modal-title').textContent = '复拍比对';
            document.getElementById('defect-modal-actions').innerHTML = 
                '<button class="btn-default" onclick="helpers.closeModal(\'defect-modal\')">关闭</button>';
            helpers.showModal('defect-modal');
        } catch (error) {
            helpers.showToast('加载比对结果失败: ' + error.message, 'error');
        }
    }
};
