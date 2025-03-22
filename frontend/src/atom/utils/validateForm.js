// src/pages/utils/validateForm.js

export const validateForm = (formData, setError) => {
    const { username, password, confirmPassword, name, nickName, birthDate, phoneNumber } = formData;

    // Check required fields
    if (!username || !password || !confirmPassword || !name || !nickName || !birthDate || !phoneNumber) {
        setError('모든 필드를 입력해주세요.');
        return false;
    }

    // Validate name and nickname pattern
    const namePattern = /^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/;
    if (!namePattern.test(name) || !namePattern.test(nickName)) {
        setError('특수문자를 제외하고 2자리 이상, 10자리 이하로 입력해주십시오.');
        return false;
    }

    // Validate phone number (basic validation)
    const phonePattern = /^[0-9]{10,11}$/;
    if (!phonePattern.test(phoneNumber)) {
        setError('유효한 전화번호를 입력해주세요.');
        return false;
    }

    // Validate password match
    if (password !== confirmPassword) {
        setError('비밀번호가 일치하지 않습니다.');
        return false;
    }

    return true;
};
