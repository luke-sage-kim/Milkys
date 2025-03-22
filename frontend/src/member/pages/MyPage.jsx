import React, { useEffect, useState } from 'react';
import Header from "../../main/components/ui/Header";


function MyPage() {
    // 상태 관리: 세션에서 가져온 사용자 정보 저장
    const [userData, setUserData] = useState({
        memberId: '',
        memberName: '',
        memberNickname: '',
        memberBirthday: '',
        memberPhoneNumber: '',
        memberAuth: '',
    });

    // 로그인 상태 관리
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // 세션 스토리지에서 데이터 가져오기
        const memberId = sessionStorage.getItem('memberId');
        const memberName = sessionStorage.getItem('memberName');
        const memberNickname = sessionStorage.getItem('memberNickname');
        const memberBirthday = sessionStorage.getItem('memberBirthday');
        const memberPhoneNumber = sessionStorage.getItem('memberPhoneNumber');
        const memberAuth = sessionStorage.getItem('memberAuth');

        // 로그인 상태 설정
        if (memberId) {
            setIsLoggedIn(true);  // 로그인 상태로 변경
        } else {
            setIsLoggedIn(false); // 로그인되지 않은 상태
        }

        // 가져온 데이터를 상태에 설정
        setUserData({
            memberId,
            memberName,
            memberNickname,
            memberBirthday,
            memberPhoneNumber,
            memberAuth,
        });
    }, []); // 빈 배열을 넣어 컴포넌트가 처음 마운트될 때만 실행

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
        </div>
    );
}

export default MyPage;
