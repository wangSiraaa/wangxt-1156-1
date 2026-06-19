const API_BASE = 'http://localhost:19456/api';

const api = {
    async request(path, options = {}) {
        const url = `${API_BASE}${path}`;
        const headers = {
            'Content-Type': 'application/json',
            'X-User': localStorage.getItem('currentUser') || 'drone_inspector',
            ...options.headers
        };

        try {
            const response = await fetch(url, {
                ...options,
                headers
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `请求失败: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API 请求失败:', error);
            throw error;
        }
    },

    get(path) {
        return this.request(path, { method: 'GET' });
    },

    post(path, data) {
        return this.request(path, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    put(path, data) {
        return this.request(path, {
            method: 'PUT',
            body: JSON.stringify(data || {})
        });
    },

    delete(path) {
        return this.request(path, { method: 'DELETE' });
    },

    turbines: {
        list(windFarm, status) {
            const params = new URLSearchParams();
            if (windFarm) params.set('windFarm', windFarm);
            if (status) params.set('status', status);
            return api.get(`/turbines?${params}`);
        },
        get(id) { return api.get(`/turbines/${id}`); },
        create(data) { return api.post('/turbines', data); },
        update(id, data) { return api.put(`/turbines/${id}`, data); },
        delete(id) { return api.delete(`/turbines/${id}`); }
    },

    defects: {
        list(turbineId, status) {
            const params = new URLSearchParams();
            if (turbineId) params.set('turbineId', turbineId);
            if (status) params.set('status', status);
            return api.get(`/defects?${params}`);
        },
        get(id) { return api.get(`/defects/${id}`); },
        upload(data) { return api.post('/defects', data); },
        evaluate(id, data) { return api.put(`/defects/${id}/evaluate`, data); },
        review(id, data) { return api.put(`/defects/${id}/review`, data); },
        delete(id) { return api.delete(`/defects/${id}`); }
    },

    reshoots: {
        list(defectId, turbineId) {
            const params = new URLSearchParams();
            if (defectId) params.set('defectId', defectId);
            if (turbineId) params.set('turbineId', turbineId);
            return api.get(`/reshoots?${params}`);
        },
        get(id) { return api.get(`/reshoots/${id}`); },
        getComparison(id) { return api.get(`/reshoots/${id}/comparison`); },
        create(data) { return api.post('/reshoots', data); },
        complete(id, data) { return api.put(`/reshoots/${id}/complete`, data); },
        delete(id) { return api.delete(`/reshoots/${id}`); }
    },

    windows: {
        list(turbineId, defectId, status) {
            const params = new URLSearchParams();
            if (turbineId) params.set('turbineId', turbineId);
            if (defectId) params.set('defectId', defectId);
            if (status) params.set('status', status);
            return api.get(`/maintenance-windows?${params}`);
        },
        findAvailable(turbineId) {
            const params = new URLSearchParams();
            if (turbineId) params.set('turbineId', turbineId);
            return api.get(`/maintenance-windows/available?${params}`);
        },
        get(id) { return api.get(`/maintenance-windows/${id}`); },
        create(data) { return api.post('/maintenance-windows', data); },
        confirm(id) { return api.put(`/maintenance-windows/${id}/confirm`); },
        start(id, data) { return api.put(`/maintenance-windows/${id}/start`, data || {}); },
        complete(id, data) { return api.put(`/maintenance-windows/${id}/complete`, data || {}); },
        cancel(id, reason) {
            const params = new URLSearchParams();
            if (reason) params.set('reason', reason);
            return api.put(`/maintenance-windows/${id}/cancel?${params}`);
        }
    },

    outages: {
        list(turbineId, isActive) {
            const params = new URLSearchParams();
            if (turbineId) params.set('turbineId', turbineId);
            if (isActive === true || isActive === false) params.set('isActive', isActive);
            return api.get(`/outages?${params}`);
        },
        get(id) { return api.get(`/outages/${id}`); },
        create(data) { return api.post('/outages', data); },
        end(id) { return api.put(`/outages/${id}/end`); },
        checkTurbine(turbineId) { return api.get(`/outages/check/turbine/${turbineId}`); }
    },

    plans: {
        list(turbineId, status) {
            const params = new URLSearchParams();
            if (turbineId) params.set('turbineId', turbineId);
            if (status) params.set('status', status);
            return api.get(`/generation-plans?${params}`);
        },
        get(id) { return api.get(`/generation-plans/${id}`); },
        create(data) { return api.post('/generation-plans', data); },
        cancel(id, reason) {
            const params = new URLSearchParams();
            if (reason) params.set('reason', reason);
            return api.put(`/generation-plans/${id}/cancel?${params}`);
        }
    }
};
