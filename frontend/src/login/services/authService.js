const loginUser = async (username, password) => {
    const loginDto = { memberId: username, memberPw: password };

    try {
        const response = await fetch('http://localhost:8080/member/v1/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginDto),
        });

        if (!response.ok) {
            throw new Error('로그인 요청에 실패했습니다.');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('로그인 API 호출 중 오류 발생:', error);
        throw error;
    }
};

export { loginUser };
