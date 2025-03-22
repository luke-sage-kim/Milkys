// Header.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';

// 로그인 버튼
function LoginButton() {
    return <button onClick={() => window.location.href = '/login'}>로그인</button>;
}

// 로그아웃 버튼
function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await fetch('http://localhost:8080/member/v1/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                console.log('로그아웃 성공');
                sessionStorage.removeItem('memberId');
                sessionStorage.removeItem('memberNickname');
                navigate('/', { replace: true });
                window.location.reload(); // 새로 고침
            } else {
                console.error('로그아웃 실패');
            }
        } catch (error) {
            console.error('로그아웃 API 호출 중 오류 발생:', error);
        }
    };

    return <button onClick={handleLogout}>로그아웃</button>;
}
// 마이페이지 버튼
function MyPageButton() {
    const navigate = useNavigate();

    const handleNavigateToMyPage = () => {
        navigate('/mypage'); // MyPage로 이동
    };

    return <button onClick={handleNavigateToMyPage}>마이페이지</button>;
}
// Header 컴포넌트
function Header({ isLoggedIn, nickname }) {

    const navigate = useNavigate();

    // Milkys 클릭 시 메인 페이지로 이동
    const handleNavigateToMainPage = () => {
        navigate('/'); // MainPage로 이동
    };

    return (
        <header className="header">
            <div onClick={handleNavigateToMainPage} style={{ cursor: 'pointer', fontWeight: 'bold', fontSize: '1.5rem' }}>
                Milkys
            </div>

            <div className="login-button-container">
                {isLoggedIn ? (
                    <>
                        <span>{nickname}님 환영합니다!</span>
                        <MyPageButton />
                        <LogoutButton />
                    </>
                ) : (
                    <LoginButton />
                )}
            </div>

            <nav className="navbar">
                <a href="#home" className="nav-item">Music</a>
                <a href="#about" className="nav-item">Recordings</a>
                <a href="#services" className="nav-item">Gallery</a>
                <a href="#contact" className="nav-item">Board</a>
            </nav>
        </header>
    );
}

export default Header;
