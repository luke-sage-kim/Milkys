// src/services/signUp.js

export const signUp = async (formData) => {
    try {
        const response = await fetch('http://localhost:8080/member/v1/sign', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                memberId: formData.username,
                memberPw: formData.password,
                memberName: formData.name,
                memberNickname: formData.nickName,
                memberBirthday: formData.birthDate,
                memberPhoneNumber: formData.phoneNumber,
            }),
        });

        if (!response.ok) {
            throw new Error('회원가입 실패');
        }

        const data = await response.json();
        return { success: true, data };
    } catch (err) {
        console.error('회원가입 중 오류 발생:', err);
        return { success: false, error: '회원가입 중 오류가 발생했습니다.' };
    }
};
