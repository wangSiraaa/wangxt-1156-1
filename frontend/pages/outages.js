const outages = {
    async loadOutages() {
        const statusFilter = document.getElementById('outage-filter-status')?.value || '';

        try {
            const isActive = statusFilter === 'active' ? true : (statusFilter === 'ended' ? false : null);
        const res = await api.outages.list(null, isActive);
            const outages = res.data || [];

            const container = document.getElementById('outage-list');
            if (outages.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无停机记录</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>停机编号</th>
                            <th>机组ID</th>
                            <th>缺陷ID</th>
                            <th>停机原因</th>
                            <th>状态</th>
                            <th>计划开始</th>
                            <th>实际开始</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${outages.map(o => `
                            <tr>
                                <td>${helpers.escapeHtml(o.outageCode || '-')}</td>
                                <td>${o.turbineId || '-'}</td>
                                <td>${o.defectId || '-'}</td>
                                <td>${helpers.escapeHtml(o.outageReason || '-')}</td>
                                <td><span class="status-tag ${o.isActive ? 'status-error' : 'status-default'}">${o.isActive ? '停机中' : '已结束'}</span></td>
                                <td>${helpers.formatDateTime(o.plannedStartTime)}</td>
                                <td>${helpers.formatDateTime(o.actualStartTime)}</td>
                                <td>
                                    ${o.isActive ? `<button class="btn-link" onclick="outages.endOutage(${o.id})">结束停机</button>` : ''}
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载停机记录失败: ' + error.message, 'error');
        }
    },

    async endOutage(outageId) {
        if (!confirm('确认结束本次停机吗？结束后机组将恢复备用状态。')) return;
        try {
            await api.outages.end(outageId);
            helpers.showToast('停机已结束');
            this.loadOutages();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('结束失败: ' + error.message, 'error');
        }
    }
};
