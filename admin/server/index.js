const express = require('express')
const path = require('path')
const app = express()
const history = require('connect-history-api-fallback');
const { createProxyMiddleware } = require('http-proxy-middleware');

const { PORT = 9526 } = process.env

app.use(history());
app.use(express.static(path.join(__dirname, '../dist')));

app.use('/api', createProxyMiddleware({ target: 'http://localhost:8080', changeOrigin: true }));
app.use('/oauth', createProxyMiddleware({ target: 'http://localhost:8080', changeOrigin: true }));

// Go
app.listen(PORT, '127.0.0.1', () => console.log(`App running on port ${PORT}!`));
