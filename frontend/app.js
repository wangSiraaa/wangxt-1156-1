const app = {
    currentPage: 'dashboard',

    init() {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            document.getElementById('roleSelector').value = savedUser;
        }

        this.loadAllTurbineSelects();
        this.refreshCurrentPage();
    },

    switchRole(role) {
        localStorage.setItem('currentUser', role);
        helpers.showToast('已切换角色: ' + this.getRoleName(role));
        this.refreshCurrentPage();
    },

    getRoleName(role) {
        const map = {
            'drone_inspector': '无人机巡检员',
            'maintenance_team': '检修班',
            'station_manager': '场站负责人',
            'admin': '系统管理员'
        };
        return map[role] || role;
    },

    showPage(pageName) {
        document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
        document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));

        const page = document.getElementById('page-' + pageName);
        if (page) page.classList.add('active');

        const tabs = document.querySelectorAll('.nav-tab');
        tabs.forEach(tab => {
            if (tab.getAttribute('onclick')?.includes(pageName)) {
                tab.classList.add('active');
            }
        });

        this.currentPage = pageName;
        this.refreshPage(pageName);
    },

    refreshCurrentPage() {
        this.refreshPage(this.currentPage);
    },

    refreshPage(pageName) {
        switch (pageName) {
            case 'dashboard':
                dashboard.refresh();
                break;
            case 'defects':
                defects.loadDefects();
                break;
            case 'reshoots':
                reshoots.loadReshoots();
                break;
            case 'windows':
                windows.loadWindows();
                break;
            case 'outages':
                outages.loadOutages();
                break;
            case 'turbines':
                turbines.loadTurbines();
                break;
            case 'plans':
                plans.loadPlans();
                break;
        }
    },

    async loadAllTurbineSelects() {
        try {
            const res = await api.turbines.list();
            const turbines = res.data || [];

            const selectIds = [
                'defect-filter-turbine',
                'reshoot-filter-turbine'
            ];

            selectIds.forEach(id => {
                const select = document.getElementById(id);
                if (select && select.options.length <= 1) {
                    turbines.forEach(t => {
                        const opt = document.createElement('option');
                        opt.value = t.id;
                        opt.textContent = t.turbineCode + ' - ' + (t.turbineName || '');
                        select.appendChild(opt);
                    });
                }
            });
        } catch (e) {
            console.warn('加载机组选项失败', e);
        }
    }
};

function showPage(pageName) {
    app.showPage(pageName);
}

function switchRole(role) {
    app.switchRole(role);
}

document.addEventListener('DOMContentLoaded', () => {
    app.init();
});

document.querySelectorAll('.modal').forEach(modal => {
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.classList.remove('show');
        }
    });
});
