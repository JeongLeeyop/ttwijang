const express = require('express')
const path = require('path')
const app = express()
const history = require('connect-history-api-fallback');
const { createProxyMiddleware } = require('http-proxy-middleware');

const { PORT = 3000 } = process.env

// 프록시를 history 미들웨어보다 먼저 등록해야 API 요청이 백엔드로 전달됨
app.use('/api', createProxyMiddleware({ target: 'http://localhost:8080', changeOrigin: true }));
app.use('/oauth', createProxyMiddleware({ target: 'http://localhost:8080', changeOrigin: true }));

app.use(history());
app.use(express.static(path.join(__dirname, '../dist')));

// Go
app.listen(PORT, '127.0.0.1', () => console.log(`App running on port ${PORT}!`));
