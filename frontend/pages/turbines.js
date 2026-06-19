const turbines = {
    async loadTurbines() {
        try {
            const res = await api.turbines.list();
            const turbines = res.data || [];

            const container = document.getElementById('turbine-list');
            if (turbines.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无机组数据</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>机组编号</th>
                            <th>机组名称</th>
                            <th>风电场</th>
                            <th>容量 (kW)</th>
                            <th>状态</th>
                            <th>位置</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${turbines.map(t => `
                            <tr>
                                <td>${helpers.escapeHtml(t.turbineCode || '-')}</td>
                                <td>${helpers.escapeHtml(t.turbineName || '-')}</td>
                                <td>${helpers.escapeHtml(t.windFarm || '-')}</td>
                                <td>${t.capacityKw || '-'}</td>
                                <td><span class="status-tag ${helpers.getTurbineStatusClass(t.status)}">${helpers.getTurbineStatusText(t.status)}</span></td>
                                <td>${helpers.escapeHtml(t.locationDesc || '-')}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载机组列表失败: ' + error.message, 'error');
        }
    }
};
