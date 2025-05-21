import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom'; // BrowserRouter 가져오기

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <React.StrictMode>
        {/* BrowserRouter로 App을 감싸서 라우팅을 활성화 */}
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </React.StrictMode>
);

// 성능 측정을 시작하려면 아래 코드 사용
reportWebVitals();
