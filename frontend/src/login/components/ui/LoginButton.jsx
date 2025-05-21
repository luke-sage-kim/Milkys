// src/components/LoginButton.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

function LoginButton() {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate('/login'); // 로그인 페이지로 이동
  };

  return (
    <button onClick={handleLoginClick} className="login-button">Login</button>
  );
}

export default LoginButton;
