import api from './api';

class BackupService {
    async exportBackup() {
        try {
            const response = await api.get('/api/backups/export', {
                responseType: 'blob'
            });
            return response;
        } catch (error) {
            console.error('Error exporting backup:', error);
            throw error;
        }
    }

    async listBackups() {
        try {
            const response = await api.get('/api/backups');
            return response.data;
        } catch (error) {
            console.error('Error listing backups:', error);
            throw error;
        }
    }

    async importBackup(filename) {
        try {
            const response = await api.post('/api/backups/import', null, {
                params: { filename }
            });
            return response.data;
        } catch (error) {
            console.error('Error importing backup:', error);
            throw error;
        }
    }
}

export default new BackupService();