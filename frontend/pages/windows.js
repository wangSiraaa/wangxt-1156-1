const windows = {
    async loadWindows() {
        const status = document.getElementById('window-filter-status')?.value || '';

        try {
            const res = await api.windows.list(null, null, status || null);
            const windows = res.data || [];

            const container = document.getElementById('window-list');
            if (windows.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无检修窗口</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>窗口编号</th>
                            <th>机组ID</th>
                            <th>预约状态</th>
                            <th>状态</th>
                            <th>计划开始</th>
                            <th>预计风速</th>
                            <th>工单编号</th>
                            <th>复核结论</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${windows.map(w => `
                            <tr>
                                <td>${helpers.escapeHtml(w.windowCode || '-')}</td>
                                <td>${w.turbineId || '-'}</td>
                                <td>
                                    ${w.isReservation ? '<span class="status-tag status-info">预约</span>' : '<span class="status-tag status-default">正常</span>'}
                                </td>
                                <td><span class="status-tag ${helpers.getWindowStatusClass(w.status)}">${helpers.getWindowStatusText(w.status)}</span></td>
                                <td>${helpers.formatDateTime(w.plannedStartTime)}</td>
                                <td>${w.expectedWindSpeed || '-'} m/s</td>
                                <td>${helpers.escapeHtml(w.workOrderCode || '-')}</td>
                                <td>${helpers.escapeHtml(w.reviewConclusion || '-')}</td>
                                <td class="action-btns">
                                    ${w.status === 'PROPOSED' ? `<button class="btn-link" onclick="windows.confirmWindow(${w.id})">确认</button>` : ''}
                                    ${w.status === 'CONFIRMED' ? `<button class="btn-link" onclick="windows.startWindow(${w.id})">开始</button>` : ''}
                                    ${w.status === 'IN_PROGRESS' ? `<button class="btn-link" onclick="windows.completeWindow(${w.id})">完成</button>` : ''}
                                    ${w.status !== 'COMPLETED' ? `<button class="btn-link" onclick="windows.cancelWindow(${w.id})">取消</button>` : ''}
                                    <button class="btn-link" onclick="windows.showDetail(${w.id})">详情</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载检修窗口失败: ' + error.message, 'error');
        }
    },

    async showDetail(windowId) {
        try {
            const res = await api.windows.get(windowId);
            const w = res.data;

            const content = `
                <div class="detail-section">
                    <h4>基本信息</h4>
                    <div class="detail-row"><span class="detail-label">窗口编号:</span><span class="detail-value">${helpers.escapeHtml(w.windowCode || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">机组ID:</span><span class="detail-value">${w.turbineId || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">关联缺陷:</span><span class="detail-value">${w.defectId || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">窗口类型:</span><span class="detail-value">${w.windowType || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">预约状态:</span><span class="detail-value">
                        ${w.isReservation ? '<span class="status-tag status-info">预约窗口</span>' : '<span class="status-tag status-default">正常窗口</span>'}
                    </span></div>
                    <div class="detail-row"><span class="detail-label">状态:</span><span class="detail-value"><span class="status-tag ${helpers.getWindowStatusClass(w.status)}">${helpers.getWindowStatusText(w.status)}</span></span></div>
                </div>
                ${w.isReservation ? `
                <div class="detail-section">
                    <h4>预约信息</h4>
                    <div class="detail-row"><span class="detail-label">预约到期时间:</span><span class="detail-value">${helpers.formatDateTime(w.reservationExpireTime)}</span></div>
                </div>
                ` : ''}
                <div class="detail-section">
                    <h4>计划信息</h4>
                    <div class="detail-row"><span class="detail-label">计划开始:</span><span class="detail-value">${helpers.formatDateTime(w.plannedStartTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">计划结束:</span><span class="detail-value">${helpers.formatDateTime(w.plannedEndTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">预计风速:</span><span class="detail-value">${w.expectedWindSpeed || '-'} m/s</span></div>
                </div>
                ${w.status === 'IN_PROGRESS' || w.status === 'COMPLETED' ? `
                <div class="detail-section">
                    <h4>实际信息</h4>
                    <div class="detail-row"><span class="detail-label">实际开始:</span><span class="detail-value">${helpers.formatDateTime(w.actualStartTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">实际结束:</span><span class="detail-value">${helpers.formatDateTime(w.actualEndTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">实际风速:</span><span class="detail-value">${w.actualWindSpeed || '-'} m/s</span></div>
                </div>
                ` : ''}
                ${w.workOrderCode || w.reviewConclusion ? `
                <div class="detail-section">
                    <h4>工单与复核信息</h4>
                    <div class="detail-row"><span class="detail-label">工单编号:</span><span class="detail-value">${helpers.escapeHtml(w.workOrderCode || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">工单链接:</span><span class="detail-value">${w.workOrderUrl ? `<a href="${w.workOrderUrl}" target="_blank">查看工单</a>` : '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">复核结论:</span><span class="detail-value">${helpers.escapeHtml(w.reviewConclusion || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">复核意见:</span><span class="detail-value">${helpers.escapeHtml(w.reviewOpinion || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">复核人:</span><span class="detail-value">${helpers.escapeHtml(w.reviewer || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">复核时间:</span><span class="detail-value">${helpers.formatDateTime(w.reviewTime)}</span></div>
                </div>
                ` : ''}
                <div class="detail-section">
                    <h4>检修内容</h4>
                    <p style="font-size: 13px; color: #595959;">${helpers.escapeHtml(w.maintenanceContent || '无')}</p>
                </div>
                <div class="detail-section">
                    <h4>评估信息</h4>
                    <div class="detail-row"><span class="detail-label">评估人:</span><span class="detail-value">${helpers.escapeHtml(w.evaluator || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">评估时间:</span><span class="detail-value">${helpers.formatDateTime(w.evaluationTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">评估意见:</span><span class="detail-value">${helpers.escapeHtml(w.evaluationOpinion || '-')}</span></div>
                </div>
                ${w.remark ? `
                <div class="detail-section">
                    <h4>备注</h4>
                    <p style="font-size: 13px; color: #595959;">${helpers.escapeHtml(w.remark)}</p>
                </div>
                ` : ''}
            `;

            document.getElementById('defect-detail-content').innerHTML = content;
            document.getElementById('modal-title').textContent = '检修窗口详情';
            document.getElementById('defect-modal-actions').innerHTML = 
                '<button class="btn-default" onclick="helpers.closeModal(\'defect-modal\')">关闭</button>';
            helpers.showModal('defect-modal');
        } catch (error) {
            helpers.showToast('加载窗口详情失败: ' + error.message, 'error');
        }
    },

    async confirmWindow(windowId) {
        if (!confirm('确认要将该检修窗口设为已确认状态吗？')) return;
        try {
            await api.windows.confirm(windowId);
            helpers.showToast('窗口已确认');
            this.loadWindows();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('确认失败: ' + error.message, 'error');
        }
    },

    async startWindow(windowId) {
        const windSpeed = prompt('请输入实际风速 (m/s):', '6.0');
        if (windSpeed === null) return;
        try {
            await api.windows.start(windowId, { actualWindSpeed: Number(windSpeed) });
            helpers.showToast('检修已开始');
            this.loadWindows();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('开始失败: ' + error.message, 'error');
        }
    },

    async completeWindow(windowId) {
        if (!confirm('确认完成本次检修吗？')) return;

        const workOrderCode = prompt('请输入工单编号:');
        if (workOrderCode === null) return;

        const workOrderUrl = prompt('请输入工单链接 (可选):');
        if (workOrderUrl === null) return;

        const maintenancePhotos = prompt('请输入检修照片链接，多个用逗号分隔 (可选):');
        if (maintenancePhotos === null) return;

        const reviewConclusion = prompt('请输入复核结论 (已修复/未修复/部分修复/需进一步处理):', '已修复');
        if (reviewConclusion === null) return;

        const reviewOpinion = prompt('请输入复核意见:');
        if (reviewOpinion === null) return;

        try {
            await api.windows.complete(windowId, {
                workOrderCode: workOrderCode,
                workOrderUrl: workOrderUrl,
                maintenancePhotos: maintenancePhotos,
                reviewConclusion: reviewConclusion,
                reviewOpinion: reviewOpinion
            });
            helpers.showToast('检修已完成，回填信息已保存');
            this.loadWindows();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('完成失败: ' + error.message, 'error');
        }
    },

    async cancelWindow(windowId) {
        const reason = prompt('请输入取消原因:');
        if (reason === null) return;
        try {
            await api.windows.cancel(windowId, reason);
            helpers.showToast('窗口已取消');
            this.loadWindows();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('取消失败: ' + error.message, 'error');
        }
    }
};

function showWindowForm() {
    loadTurbineSelectOptions('window-form-turbine');
    helpers.showModal('window-form-modal');
}

function toggleReservationFields() {
    const isReservation = document.getElementById('window-form-reservation').checked;
    const reservationFields = document.getElementById('reservation-fields');
    reservationFields.style.display = isReservation ? 'block' : 'none';
}

async function submitWindowForm(event) {
    event.preventDefault();
    
    const turbineId = Number(document.getElementById('window-form-turbine').value);
    if (!turbineId) {
        helpers.showToast('请选择机组', 'warning');
        return;
    }

    const isReservation = document.getElementById('window-form-reservation').checked;

    const data = {
        turbineId: turbineId,
        defectId: document.getElementById('window-form-defect').value 
            ? Number(document.getElementById('window-form-defect').value) 
            : null,
        plannedStartTime: document.getElementById('window-form-start').value || null,
        plannedEndTime: document.getElementById('window-form-end').value || null,
        expectedWindSpeed: document.getElementById('window-form-wind').value 
            ? Number(document.getElementById('window-form-wind').value) 
            : null,
        windowType: document.getElementById('window-form-type').value,
        maintenanceContent: document.getElementById('window-form-content').value,
        maintenanceTeam: document.getElementById('window-form-team').value,
        evaluationOpinion: document.getElementById('window-form-opinion').value,
        isReservation: isReservation,
        reservationExpireTime: isReservation 
            ? (document.getElementById('window-form-reservation-expire').value || null)
            : null
    };

    try {
        await api.windows.create(data);
        helpers.showToast(isReservation ? '预约窗口创建成功' : '检修窗口创建成功');
        helpers.closeModal('window-form-modal');
        windows.loadWindows();
        document.getElementById('window-form').reset();
        document.getElementById('reservation-fields').style.display = 'none';
    } catch (error) {
        helpers.showToast('创建失败: ' + error.message, 'error');
    }
}
