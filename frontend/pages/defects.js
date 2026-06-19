const defects = {
    currentDefectId: null,

    async loadDefects() {
        const status = document.getElementById('defect-filter-status')?.value || '';
        const turbineId = document.getElementById('defect-filter-turbine')?.value || '';

        try {
            const res = await api.defects.list(
                turbineId ? Number(turbineId) : null,
                status || null
            );
            const defects = res.data || [];

            const container = document.getElementById('defect-list');
            if (defects.length === 0) {
                container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📭</div><div>暂无缺陷记录</div></div>';
                return;
            }

            container.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>缺陷编号</th>
                            <th>机组</th>
                            <th>叶片</th>
                            <th>类型</th>
                            <th>状态</th>
                            <th>严重程度</th>
                            <th>上传时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${defects.map(d => `
                            <tr>
                                <td>${helpers.escapeHtml(d.defectCode || '-')}</td>
                                <td>${d.turbineId || '-'}</td>
                                <td>${d.bladeNumber || '-'}</td>
                                <td>${helpers.getDefectTypeText(d.defectType)}</td>
                                <td><span class="status-tag ${helpers.getDefectStatusClass(d.status)}">${helpers.getDefectStatusText(d.status)}</span></td>
                                <td>${helpers.getSeverityText(d.severityLevel)}</td>
                                <td>${helpers.formatDateTime(d.createTime)}</td>
                                <td>
                                    <button class="btn-link" onclick="defects.showDetail(${d.id})">详情</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            helpers.showToast('加载缺陷列表失败: ' + error.message, 'error');
        }
    },

    async showDetail(defectId) {
        this.currentDefectId = defectId;
        try {
            const res = await api.defects.get(defectId);
            const defect = res.data;

            let photosHtml = '';
            if (defect.photoUrls) {
                const photos = defect.photoUrls.split(',').filter(Boolean);
                photosHtml = `<div class="photo-gallery">
                    ${photos.map((_, i) => `<div class="photo-thumb">🖼️</div>`).join('')}
                </div>`;
            }

            let reshootsHtml = '';
            try {
                const reshootRes = await api.reshoots.list(defectId, null);
                const reshoots = reshootRes.data || [];
                if (reshoots.length > 0) {
                    reshootsHtml = `
                        <div class="detail-section">
                            <h4>复拍记录</h4>
                            ${reshoots.map(r => `
                                <div style="margin-bottom: 8px; padding: 8px; background: #fafafa; border-radius: 4px;">
                                    <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                                        <span>${helpers.escapeHtml(r.reshootCode || '')}</span>
                                        <span class="status-tag ${r.isCompleted ? 'status-success' : 'status-warning'}">${r.isCompleted ? '已完成' : '待完成'}</span>
                                    </div>
                                    <div style="font-size: 12px; color: #8c8c8c;">
                                        ${r.isCompleted ? '完成时间: ' + helpers.formatDateTime(r.actualTime) : '计划时间: ' + helpers.formatDateTime(r.scheduledTime)}
                                        | 第${r.reshootOrder}次复拍
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    `;
                }
            } catch (e) { console.warn('加载复拍记录失败', e); }

            const content = `
                <div class="detail-section">
                    <h4>基本信息</h4>
                    <div class="detail-row"><span class="detail-label">缺陷编号:</span><span class="detail-value">${helpers.escapeHtml(defect.defectCode || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">机组ID:</span><span class="detail-value">${defect.turbineId || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">叶片编号:</span><span class="detail-value">${defect.bladeNumber || '-'}</span></div>
                    <div class="detail-row"><span class="detail-label">叶片位置:</span><span class="detail-value">${helpers.escapeHtml(defect.bladePosition || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">缺陷类型:</span><span class="detail-value">${helpers.getDefectTypeText(defect.defectType)}</span></div>
                    <div class="detail-row"><span class="detail-label">当前状态:</span><span class="detail-value"><span class="status-tag ${helpers.getDefectStatusClass(defect.status)}">${helpers.getDefectStatusText(defect.status)}</span></span></div>
                    <div class="detail-row"><span class="detail-label">严重程度:</span><span class="detail-value">${helpers.getSeverityText(defect.severityLevel)}</span></div>
                    <div class="detail-row"><span class="detail-label">缺陷尺寸:</span><span class="detail-value">${defect.defectSize || '-'} ${defect.sizeUnit || ''}</span></div>
                </div>
                <div class="detail-section">
                    <h4>缺陷描述</h4>
                    <p style="font-size: 13px; color: #595959;">${helpers.escapeHtml(defect.defectDescription || '无')}</p>
                    ${photosHtml}
                </div>
                <div class="detail-section">
                    <h4>巡检信息</h4>
                    <div class="detail-row"><span class="detail-label">巡检员:</span><span class="detail-value">${helpers.escapeHtml(defect.inspector || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">巡检时间:</span><span class="detail-value">${helpers.formatDateTime(defect.inspectionTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">巡检风速:</span><span class="detail-value">${defect.windSpeedInspection || '-'} m/s</span></div>
                </div>
                ${defect.evaluationOpinion ? `
                <div class="detail-section">
                    <h4>评估信息</h4>
                    <div class="detail-row"><span class="detail-label">评估人:</span><span class="detail-value">${helpers.escapeHtml(defect.evaluator || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">评估时间:</span><span class="detail-value">${helpers.formatDateTime(defect.evaluationTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">评估意见:</span><span class="detail-value">${helpers.escapeHtml(defect.evaluationOpinion || '-')}</span></div>
                </div>
                ` : ''}
                ${defect.reviewConclusion ? `
                <div class="detail-section">
                    <h4>复核信息</h4>
                    <div class="detail-row"><span class="detail-label">复核结论:</span><span class="detail-value">${helpers.getReviewConclusionText(defect.reviewConclusion)}</span></div>
                    <div class="detail-row"><span class="detail-label">复核人:</span><span class="detail-value">${helpers.escapeHtml(defect.reviewer || '-')}</span></div>
                    <div class="detail-row"><span class="detail-label">复核时间:</span><span class="detail-value">${helpers.formatDateTime(defect.reviewTime)}</span></div>
                    <div class="detail-row"><span class="detail-label">复核意见:</span><span class="detail-value">${helpers.escapeHtml(defect.reviewOpinion || '-')}</span></div>
                </div>
                ` : ''}
                ${reshootsHtml}
            `;

            document.getElementById('defect-detail-content').innerHTML = content;
            document.getElementById('modal-title').textContent = '缺陷详情';

            const actions = [];
            const role = localStorage.getItem('currentUser') || 'drone_inspector';

            if ((role === 'maintenance_team' || role === 'station_manager') 
                && (defect.status === 'PENDING_EVALUATION' || defect.status === 'PENDING_RESHOOT')) {
                actions.push('<button class="btn-default" onclick="defects.showEvaluateForm()">评估</button>');
            }

            if (role === 'station_manager' && defect.status === 'PENDING_MAINTENANCE') {
                actions.push('<button class="btn-primary" onclick="defects.showReviewForm()">复核</button>');
            }

            if (defect.status === 'PENDING_RESHOOT') {
                actions.push('<button class="btn-default" onclick="defects.createReshoot()">创建复拍</button>');
            }

            actions.push('<button class="btn-default" onclick="helpers.closeModal(\'defect-modal\')">关闭</button>');

            document.getElementById('defect-modal-actions').innerHTML = actions.join('');
            helpers.showModal('defect-modal');
        } catch (error) {
            helpers.showToast('加载缺陷详情失败: ' + error.message, 'error');
        }
    },

    showEvaluateForm() {
        const content = `
            <form id="evaluate-form" onsubmit="defects.submitEvaluate(event)">
                <div class="form-group">
                    <label>评估意见</label>
                    <textarea id="eval-opinion" rows="3" placeholder="请输入评估意见"></textarea>
                </div>
                <div class="form-group">
                    <label>严重程度</label>
                    <select id="eval-severity">
                        <option value="LOW">低</option>
                        <option value="MEDIUM">中</option>
                        <option value="HIGH">高</option>
                        <option value="CRITICAL">严重</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>
                        <input type="checkbox" id="eval-need-reshoot"> 需要复拍
                    </label>
                </div>
                <div class="form-group" id="eval-reason-group" style="display: none;">
                    <label>复拍原因</label>
                    <textarea id="eval-reshoot-reason" rows="2" placeholder="请输入复拍原因"></textarea>
                </div>
                <button type="submit" class="btn-primary btn-full">提交评估</button>
            </form>
        `;
        document.getElementById('defect-detail-content').innerHTML = content;
        document.getElementById('modal-title').textContent = '缺陷评估';
        document.getElementById('defect-modal-actions').innerHTML = 
            '<button class="btn-default" onclick="defects.showDetail(defects.currentDefectId)">返回</button>';

        document.getElementById('eval-need-reshoot').addEventListener('change', function() {
            document.getElementById('eval-reason-group').style.display = this.checked ? 'block' : 'none';
        });
    },

    async submitEvaluate(event) {
        event.preventDefault();
        const data = {
            evaluationOpinion: document.getElementById('eval-opinion').value,
            severityLevel: document.getElementById('eval-severity').value,
            needReshoot: document.getElementById('eval-need-reshoot').checked,
            reshootReason: document.getElementById('eval-reshoot-reason').value
        };

        try {
            await api.defects.evaluate(this.currentDefectId, data);
            helpers.showToast('评估提交成功');
            helpers.closeModal('defect-modal');
            this.loadDefects();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('评估失败: ' + error.message, 'error');
        }
    },

    showReviewForm() {
        const content = `
            <form id="review-form" onsubmit="defects.submitReview(event)">
                <div class="form-group">
                    <label>复核结论 <span class="required">*</span></label>
                    <select id="review-conclusion" required>
                        <option value="">请选择</option>
                        <option value="CONFIRMED_DEFECT">确认缺陷</option>
                        <option value="FALSE_ALARM">误报</option>
                        <option value="NEED_OBSERVATION">待观察</option>
                        <option value="NEED_MAINTENANCE">需检修</option>
                        <option value="NEED_IMMEDIATE_MAINTENANCE">需立即检修</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>复核意见</label>
                    <textarea id="review-opinion" rows="3" placeholder="请输入复核意见"></textarea>
                </div>
                <div class="form-group">
                    <label>
                        <input type="checkbox" id="review-need-outage"> 需要停机检修
                    </label>
                </div>
                <div class="form-row" id="review-outage-dates" style="display: none;">
                    <div class="form-group">
                        <label>计划停机开始时间</label>
                        <input type="datetime-local" id="review-outage-start">
                    </div>
                    <div class="form-group">
                        <label>计划停机结束时间</label>
                        <input type="datetime-local" id="review-outage-end">
                    </div>
                </div>
                <button type="submit" class="btn-primary btn-full">提交复核</button>
            </form>
        `;
        document.getElementById('defect-detail-content').innerHTML = content;
        document.getElementById('modal-title').textContent = '缺陷复核';
        document.getElementById('defect-modal-actions').innerHTML = 
            '<button class="btn-default" onclick="defects.showDetail(defects.currentDefectId)">返回</button>';

        document.getElementById('review-need-outage').addEventListener('change', function() {
            document.getElementById('review-outage-dates').style.display = this.checked ? 'grid' : 'none';
        });
    },

    async submitReview(event) {
        event.preventDefault();
        const conclusion = document.getElementById('review-conclusion').value;
        if (!conclusion) {
            helpers.showToast('请选择复核结论', 'warning');
            return;
        }

        const needOutage = document.getElementById('review-need-outage').checked;
        const plannedStart = document.getElementById('review-outage-start').value;
        const plannedEnd = document.getElementById('review-outage-end').value;

        const data = {
            reviewConclusion: conclusion,
            reviewOpinion: document.getElementById('review-opinion').value,
            needOutage: needOutage
        };

        if (needOutage) {
            if (plannedStart) data.plannedOutageStartTime = plannedStart;
            if (plannedEnd) data.plannedOutageEndTime = plannedEnd;
        }

        try {
            await api.defects.review(this.currentDefectId, data);
            helpers.showToast('复核提交成功');
            helpers.closeModal('defect-modal');
            this.loadDefects();
            dashboard.refresh();
        } catch (error) {
            helpers.showToast('复核失败: ' + error.message, 'error');
        }
    },

    async createReshoot() {
        try {
            await api.reshoots.create({
                defectId: this.currentDefectId,
                reshootReason: '缺陷评估后需复拍确认'
            });
            helpers.showToast('复拍任务创建成功');
            this.showDetail(this.currentDefectId);
        } catch (error) {
            helpers.showToast('创建复拍失败: ' + error.message, 'error');
        }
    }
};

function showDefectForm() {
    loadTurbineSelectOptions('form-turbine');
    helpers.showModal('defect-form-modal');
}

async function submitDefectForm(event) {
    event.preventDefault();
    const data = {
        turbineId: Number(document.getElementById('form-turbine').value),
        bladeNumber: Number(document.getElementById('form-blade').value),
        bladePosition: document.getElementById('form-position').value,
        defectType: document.getElementById('form-type').value,
        defectDescription: document.getElementById('form-description').value,
        severityLevel: document.getElementById('form-severity').value,
        photoUrls: document.getElementById('form-photos').value,
        shootingAngle: document.getElementById('form-angle').value 
            ? Number(document.getElementById('form-angle').value) 
            : null,
        shootingAzimuth: document.getElementById('form-azimuth').value 
            ? Number(document.getElementById('form-azimuth').value) 
            : null,
        windSpeedInspection: document.getElementById('form-wind-speed').value 
            ? Number(document.getElementById('form-wind-speed').value) 
            : null
    };

    if (!data.turbineId) {
        helpers.showToast('请选择机组', 'warning');
        return;
    }

    try {
        await api.defects.upload(data);
        helpers.showToast('缺陷上传成功');
        helpers.closeModal('defect-form-modal');
        defects.loadDefects();
        dashboard.refresh();
        document.getElementById('defect-form').reset();
    } catch (error) {
        helpers.showToast('上传失败: ' + error.message, 'error');
    }
}

async function loadTurbineSelectOptions(selectId) {
    try {
        const res = await api.turbines.list();
        const turbines = res.data || [];
        const select = document.getElementById(selectId);
        if (select) {
            const currentValue = select.value;
            const firstOption = select.querySelector('option');
            select.innerHTML = '';
            if (firstOption) select.appendChild(firstOption);
            turbines.forEach(t => {
                const opt = document.createElement('option');
                opt.value = t.id;
                opt.textContent = t.turbineCode + ' - ' + (t.turbineName || '');
                select.appendChild(opt);
            });
            if (currentValue) select.value = currentValue;
        }
    } catch (e) {
        console.warn('加载机组列表失败', e);
    }
}
