import React, { useEffect, useState } from 'react';
import Header from "../../main/components/ui/Header";
import { useNavigate } from 'react-router-dom';
import useSessionData from "../../atom/components/MySession";

function MyPage() {

    // 로그인 상태 관리
    const { isLoggedIn, userData } = useSessionData();
    // useNavigate 훅을 사용하여 페이지 이동
    const navigate = useNavigate();

    // 회원정보 수정 페이지로 이동하는 함수
    const handleNavigateToUpdateMemberPage = () => {
        navigate('/update-member');  // 회원정보 수정 페이지로 이동
    };


    return (
        <div className="my-page">
            {/* Header 컴포넌트를 추가하고 로그인 상태와 닉네임을 전달 */}
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />

            <h2>마이페이지</h2>
            <div className="profile-info">
                <p><strong>회원 ID:</strong> {userData.memberId}</p>
                <p><strong>회원 이름:</strong> {userData.memberName}</p>
                <p><strong>회원 닉네임:</strong> {userData.memberNickname}</p>
                <p><strong>회원 생년월일:</strong> {userData.memberBirthday}</p>
                <p><strong>회원 전화번호:</strong> {userData.memberPhoneNumber}</p>
                <p><strong>회원 권한:</strong> {userData.memberAuth}</p>
            </div>
            <button onClick={handleNavigateToUpdateMemberPage}>회원정보 수정하기</button>
        </div>
    );
}

export default MyPage;
