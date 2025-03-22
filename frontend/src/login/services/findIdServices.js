import axios from 'axios';

const findId = async (name, phone) => {
    try {
        const response = await axios.post('http://localhost:8080/member/v1/findId', {
            memberName: name, // DTO에서 memberName
            memberPhoneNumber: phone // DTO에서 memberPhoneNumber
        });
        return response.data;
    } catch (error) {
        throw new Error('이름 또는 전화번호를 잘못기입하셨습니다.');
    }
};

export default { findId };
