const helpers = {
    formatDateTime(dateStr) {
        if (!dateStr) return '-';
        const date = new Date(dateStr);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    formatDate(dateStr) {
        if (!dateStr) return '-';
        const date = new Date(dateStr);
        return date.toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
        });
    },

    getDefectStatusText(status) {
        const map = {
            'PENDING_EVALUATION': '待评估',
            'PENDING_RESHOOT': '待复拍',
            'PENDING_MAINTENANCE': '待检修',
            'CONFIRMED': '已确认',
            'CLOSED': '已关闭',
            'FALSE_ALARM': '误报'
        };
        return map[status] || status;
    },

    getDefectStatusClass(status) {
        const map = {
            'PENDING_EVALUATION': 'status-warning',
            'PENDING_RESHOOT': 'status-warning',
            'PENDING_MAINTENANCE': 'status-pending',
            'CONFIRMED': 'status-success',
            'CLOSED': 'status-default',
            'FALSE_ALARM': 'status-default'
        };
        return map[status] || 'status-default';
    },

    getDefectTypeText(type) {
        const map = {
            'CRACK_SUSPECTED': '疑似裂纹',
            'SURFACE_CRACK': '表面裂纹',
            'INTERNAL_CRACK': '内部裂纹',
            'CORROSION': '腐蚀',
            'ABRASION': '磨损',
            'LIGHTNING_STRIKE': '雷击损伤',
            'BLADE_DIRTY': '叶片脏污',
            'PAINT_PEELING': '油漆脱落',
            'OTHER': '其他'
        };
        return map[type] || type;
    },

    getWindowStatusText(status) {
        const map = {
            'PROPOSED': '待评估',
            'CONFIRMED': '已确认',
            'IN_PROGRESS': '进行中',
            'COMPLETED': '已完成',
            'CANCELLED': '已取消'
        };
        return map[status] || status;
    },

    getWindowStatusClass(status) {
        const map = {
            'PROPOSED': 'status-warning',
            'CONFIRMED': 'status-pending',
            'IN_PROGRESS': 'status-warning',
            'COMPLETED': 'status-success',
            'CANCELLED': 'status-default'
        };
        return map[status] || 'status-default';
    },

    getTurbineStatusText(status) {
        const map = {
            'RUNNING': '运行中',
            'STOPPED': '已停机',
            'MAINTENANCE': '检修中',
            'STANDBY': '备用',
            'FAULT': '故障'
        };
        return map[status] || status;
    },

    getTurbineStatusClass(status) {
        const map = {
            'RUNNING': 'status-success',
            'STOPPED': 'status-error',
            'MAINTENANCE': 'status-warning',
            'STANDBY': 'status-pending',
            'FAULT': 'status-error'
        };
        return map[status] || 'status-default';
    },

    getSeverityText(level) {
        const map = {
            'LOW': '低',
            'MEDIUM': '中',
            'HIGH': '高',
            'CRITICAL': '严重'
        };
        return map[level] || level || '-';
    },

    getReviewConclusionText(conclusion) {
        const map = {
            'CONFIRMED_DEFECT': '确认缺陷',
            'FALSE_ALARM': '误报',
            'NEED_OBSERVATION': '待观察',
            'NEED_MAINTENANCE': '需检修',
            'NEED_IMMEDIATE_MAINTENANCE': '需立即检修'
        };
        return map[conclusion] || conclusion || '-';
    },

    showToast(message, type = 'success') {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.className = `toast show ${type}`;
        setTimeout(() => {
            toast.className = 'toast';
        }, 3000);
    },

    showModal(id) {
        document.getElementById(id).classList.add('show');
    },

    closeModal(id) {
        document.getElementById(id).classList.remove('show');
    },

    isCrackType(defectType) {
        return ['CRACK_SUSPECTED', 'SURFACE_CRACK', 'INTERNAL_CRACK'].includes(defectType);
    },

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
};

function closeModal(modalId) {
    helpers.closeModal(modalId);
}

function showToast(message, type) {
    helpers.showToast(message, type);
}
