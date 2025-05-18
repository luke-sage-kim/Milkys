import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession"; // 세션에서 memberAuth 추출

function AdminMemReqPage() {
    const [members, setMembers] = useState([]);
    const { userData } = useSessionData(); // 로그인한 관리자 정보
    const fetchUnapprovedMembers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/member/v1/unapprovalList');
            const resultData = response.data.resultData;
            setMembers(Array.isArray(resultData) ? resultData : []);
        } catch (error) {
            console.error('가입요청 불러오기 실패:', error);
            setMembers([]); // 실패해도 안전하게 초기화
        }
    };


    useEffect(() => {
        fetchUnapprovedMembers();
    }, []);

    const handleApprove = async (memberCode) => {
        const confirm = window.confirm('해당 회원의 가입을 승인하시겠습니까?');
        if (!confirm) return;

        try {
            const response = await axios.put('http://localhost:8080/member/v1/approve', {
                memberCode: memberCode,
                memberAuth: userData.memberAuth
            });
            console.log(response.data.resultData)
            if (response.data.resultData.status === 200) {
                alert('승인이 완료되었습니다.');
                fetchUnapprovedMembers(); // 목록 갱신
            } else {
                alert('승인 처리 실패. 다시 시도해주세요.');
            }
        } catch (error) {
            console.error('승인 처리 실패:', error);
            alert('서버 오류가 발생했습니다.');
        }
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <Header />
            <div className="max-w-6xl mx-auto p-6 mt-10">
                <h1 className="text-2xl font-bold mb-6 text-center">🛂 가입 요청 목록</h1>
                {members.length === 0 ? (
                    <p className="text-center text-gray-600">가입 요청 대기중인 회원이 없습니다.</p>
                ) : (
                    <div className="overflow-x-auto bg-white shadow-md rounded-lg p-4">
                        <table className="w-full table-auto border-collapse">
                            <thead>
                            <tr className="bg-gray-200 text-gray-700">
                                <th className="p-2 border">회원번호</th>
                                <th className="p-2 border">아이디</th>
                                <th className="p-2 border">이름</th>
                                <th className="p-2 border">닉네임</th>
                                <th className="p-2 border">생년월일</th>
                                <th className="p-2 border">전화번호</th>
                                <th className="p-2 border">상태</th>
                                <th className="p-2 border">승인</th>
                            </tr>
                            </thead>
                            <tbody>
                            {members.map((member) => (
                                <tr key={member.memberCode} className="text-center hover:bg-gray-50">
                                    <td className="p-2 border">{member.memberCode}</td>
                                    <td className="p-2 border">{member.memberId}</td>
                                    <td className="p-2 border">{member.memberName}</td>
                                    <td className="p-2 border">{member.memberNickname}</td>
                                    <td className="p-2 border">{member.memberBirthday}</td>
                                    <td className="p-2 border">{member.memberPhoneNumber}</td>
                                    <td className="p-2 border text-red-500 font-semibold">{member.memberAuth}</td>
                                    <td className="p-2 border">
                                        <button
                                            onClick={() => handleApprove(member.memberCode)}
                                            className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition"
                                        >
                                            승인
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}

export default AdminMemReqPage;
