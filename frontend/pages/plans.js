const plans = {
    async loadPlans() {
        try {
            const res = await api.plans.list();
            const plans = res.data || [];

            const container = document.getElementById('plan-list');
            if (plans.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无发电计划</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>计划编号</th>
                            <th>机组ID</th>
                            <th>计划日期</th>
                            <th>计划发电量 (kWh)</th>
                            <th>计划时长 (小时)</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${plans.map(p => `
                            <tr>
                                <td>${helpers.escapeHtml(p.planCode || '-')}</td>
                                <td>${p.turbineId || '-'}</td>
                                <td>${helpers.formatDate(p.planDate)}</td>
                                <td>${p.plannedOutput || '-'}</td>
                                <td>${p.plannedHours || '-'}</td>
                                <td><span class="status-tag ${p.status === 'PUBLISHED' ? 'status-success' : p.status === 'CANCELLED' ? 'status-error' : 'status-default'}">${p.status === 'PUBLISHED' ? '已发布' : p.status === 'CANCELLED' ? '已取消' : p.status}</span></td>
                                <td>
                                    ${p.status === 'PUBLISHED' ? `<button class="btn-link" onclick="plans.cancelPlan(${p.id})">取消</button>` : ''}
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载发电计划失败: ' + error.message, 'error');
        }
    },

    async cancelPlan(planId) {
        const reason = prompt('请输入取消原因:');
        if (reason === null) return;
        try {
            await api.plans.cancel(planId, reason);
            helpers.showToast('计划已取消');
            this.loadPlans();
        } catch (error) {
            helpers.showToast('取消失败: ' + error.message, 'error');
        }
    }
};

function showPlanForm() {
    loadTurbineSelectOptions('plan-form-turbine');
    helpers.showModal('plan-form-modal');
}

async function submitPlanForm(event) {
    event.preventDefault();
    
    const turbineId = Number(document.getElementById('plan-form-turbine').value);
    if (!turbineId) {
        helpers.showToast('请选择机组', 'warning');
        return;
    }

    const data = {
        turbineId: turbineId,
        planDate: document.getElementById('plan-form-date').value || null,
        plannedOutput: document.getElementById('plan-form-output').value 
            ? Number(document.getElementById('plan-form-output').value) 
            : null,
        plannedHours: document.getElementById('plan-form-hours').value 
            ? Number(document.getElementById('plan-form-hours').value) 
            : null,
        remark: document.getElementById('plan-form-remark').value
    };

    try {
        await api.plans.create(data);
        helpers.showToast('发电计划创建成功');
        helpers.closeModal('plan-form-modal');
        plans.loadPlans();
        document.getElementById('plan-form').reset();
    } catch (error) {
        helpers.showToast('创建失败: ' + error.message, 'error');
    }
}
