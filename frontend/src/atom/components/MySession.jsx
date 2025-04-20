// src/hooks/useSessionData.js
import { useState, useEffect } from 'react';

const useSessionData = () => {
    const [userData, setUserData] = useState({
        memberId: '',
        memberName: '',
        memberNickname: '',
        memberBirthday: '',
        memberPhoneNumber: '',
        memberAuth: '',
        memberCode: 0, // 기본값을 숫자 0으로 설정
    });
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // 세션 스토리지에서 데이터 가져오기
        const memberId = sessionStorage.getItem('memberId');
        const memberName = sessionStorage.getItem('memberName');
        const memberNickname = sessionStorage.getItem('memberNickname');
        const memberBirthday = sessionStorage.getItem('memberBirthday');
        const memberPhoneNumber = sessionStorage.getItem('memberPhoneNumber');
        const memberAuth = sessionStorage.getItem('memberAuth');
        const memberCode = sessionStorage.getItem('memberCode');

        // 로그인 상태 설정
        if (memberId) {
            setIsLoggedIn(true);  // 로그인 상태로 변경
        } else {
            setIsLoggedIn(false); // 로그인되지 않은 상태
        }

        // 가져온 데이터를 상태에 설정 (memberCode를 숫자로 변환)
        setUserData({
            memberId,
            memberName,
            memberNickname,
            memberBirthday,
            memberPhoneNumber,
            memberAuth,
            memberCode: memberCode ? Number(memberCode) : 0, // 숫자로 변환
        });
    }, []); // 빈 배열을 넣어 컴포넌트가 처음 마운트될 때만 실행

    return { isLoggedIn, userData };
};

export default useSessionData;
