const dashboard = {
    async loadStats() {
        try {
            const [defectsRes, turbinesRes] = await Promise.all([
                api.defects.list(),
                api.turbines.list()
            ]);

            const defects = defectsRes.data || [];
            const turbines = turbinesRes.data || [];

            const pendingEval = defects.filter(d => d.status === 'PENDING_EVALUATION').length;
            const pendingReshoot = defects.filter(d => d.status === 'PENDING_RESHOOT').length;
            const pendingMaint = defects.filter(d => d.status === 'PENDING_MAINTENANCE').length;
            const stoppedTurbines = turbines.filter(t => t.status === 'STOPPED').length;

            document.getElementById('stat-pending-eval').textContent = pendingEval;
            document.getElementById('stat-pending-reshoot').textContent = pendingReshoot;
            document.getElementById('stat-pending-maint').textContent = pendingMaint;
            document.getElementById('stat-stopped').textContent = stoppedTurbines;
        } catch (error) {
            console.error('加载统计数据失败:', error);
        }
    },

    async loadLatestDefects() {
        try {
            const res = await api.defects.list();
            const defects = (res.data || []).slice(0, 5);

            const container = document.getElementById('latest-defects');
            if (defects.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无缺陷记录</div></div>';
                return;
            }

            container.innerHTML = defects.map(defect => `
                <div class="data-item" onclick="defects.showDetail(${defect.id})" style="cursor: pointer;">
                    <div class="data-item-title">
                        ${helpers.escapeHtml(defect.defectCode || '未知编号')}
                        <span class="status-tag ${helpers.getDefectStatusClass(defect.status)}">${helpers.getDefectStatusText(defect.status)}</span>
                    </div>
                    <div class="data-item-desc">${helpers.escapeHtml(defect.defectDescription || '无描述')}</div>
                    <div class="data-item-meta">
                        <span>叶片: ${defect.bladeNumber || '-'}</span>
                        <span>类型: ${helpers.getDefectTypeText(defect.defectType)}</span>
                        <span>上传: ${helpers.formatDateTime(defect.createTime)}</span>
                    </div>
                </div>
            `).join('');
        } catch (error) {
            console.error('加载最新缺陷失败:', error);
            document.getElementById('latest-defects').innerHTML = '<div class="empty-state">加载失败</div>';
        }
    },

    async loadPendingReshoots() {
        try {
            const res = await api.reshoots.list();
            const allReshoots = res.data || [];
            const pending = allReshoots.filter(r => !r.isCompleted).slice(0, 5);

            const container = document.getElementById('pending-reshoots');
            if (pending.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">✅</div><div>暂无待办复拍任务</div></div>';
                return;
            }

            container.innerHTML = pending.map(reshoot => `
                <div class="data-item" style="border-left-color: #faad14;">
                    <div class="data-item-title">
                        ${helpers.escapeHtml(reshoot.reshootCode || '未知编号')}
                        <span class="status-tag status-warning">待完成</span>
                    </div>
                    <div class="data-item-desc">${helpers.escapeHtml(reshoot.reshootReason || '无原因')}</div>
                    <div class="data-item-meta">
                        <span>缺陷ID: ${reshoot.defectId}</span>
                        <span>计划: ${helpers.formatDateTime(reshoot.scheduledTime)}</span>
                        <span>第${reshoot.reshootOrder || 1}次复拍</span>
                    </div>
                </div>
            `).join('');
        } catch (error) {
            console.error('加载待办复拍失败:', error);
            document.getElementById('pending-reshoots').innerHTML = '<div class="empty-state">加载失败</div>';
        }
    },

    async refresh() {
        await Promise.all([
            this.loadStats(),
            this.loadLatestDefects(),
            this.loadPendingReshoots()
        ]);
    }
};
