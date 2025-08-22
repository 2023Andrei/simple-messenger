const WebSocket = require('ws');
const http = require('http');

const server = http.createServer();
const wss = new WebSocket.Server({ server });

wss.on('connection', (ws) => {
    console.log('Клиент подключился');

    ws.on('message', (data) => {
        try {
            const message = JSON.parse(data);
            // Пересылка всем, кроме отправителя
            wss.clients.forEach(client => {
                if (client !== ws && client.readyState === WebSocket.OPEN) {
                    client.send(JSON.stringify(message));
                }
            });
        } catch (e) {
            console.error('Ошибка парсинга:', e);
        }
    });
});

server.listen(8080, () => {
    console.log('Сервер запущен на ws://localhost:8080');
});
