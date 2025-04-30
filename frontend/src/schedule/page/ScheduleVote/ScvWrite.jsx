import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useSessionData from "../../../atom/components/MySession";

const ScvWrite = () => {
    const { date } = useParams();
    const { userData } = useSessionData();
    const navigate = useNavigate();

    const [scvStart, setScvStart] = useState('');
    const [scvEnd, setScvEnd] = useState('');

    // 09:00 ~ 24:00 까지 시간 배열 생성
    const hours = Array.from({ length: 16 }, (_, i) => {
        const hour = i + 9;
        return `${String(hour).padStart(2, '0')}:00`;
    });

    const handleSubmit = async () => {
        if (!scvStart || !scvEnd) {
            alert('시작 시간과 종료 시간을 선택해주세요.');
            return;
        }

        const startHour = parseInt(scvStart.split(':')[0]);
        const endHour = parseInt(scvEnd.split(':')[0]);

        if (startHour >= endHour) {
            alert('시작 시간은 종료 시간보다 빨라야 합니다.');
            return;
        }

        const payload = {
            memberCode: userData.memberCode,
            scvDate: date,
            scvStart,
            scvEnd,
        };

        try {
            const response = await fetch('http://localhost:8080/scv/v1/write', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert('일정 투표가 성공적으로 등록되었습니다!');
                navigate(`/schedule-detail/${date}`);
            } else {
                alert('등록에 실패했습니다.');
            }
        } catch (error) {
            console.error('API 호출 중 오류 발생:', error);
            alert('서버 오류가 발생했습니다.');
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow">
            <h2 className="text-2xl font-bold mb-6 text-center">{date} 일정 투표 작성</h2>

            {/* 시작 시간 */}
            <div className="mb-4">
                <label className="block text-sm font-medium mb-2">시작 시간</label>
                <select
                    value={scvStart}
                    onChange={(e) => setScvStart(e.target.value)}
                    className="w-full border border-gray-300 rounded px-3 py-2"
                >
                    <option value="">시간 선택</option>
                    {hours.map(hour => (
                        <option key={hour} value={hour}>{hour}</option>
                    ))}
                </select>
            </div>

            {/* 종료 시간 */}
            <div className="mb-4">
                <label className="block text-sm font-medium mb-2">종료 시간</label>
                <select
                    value={scvEnd}
                    onChange={(e) => setScvEnd(e.target.value)}
                    className="w-full border border-gray-300 rounded px-3 py-2"
                >
                    <option value="">시간 선택</option>
                    {hours.map(hour => (
                        <option key={hour} value={hour}>{hour}</option>
                    ))}
                </select>
            </div>

            {/* 버튼 */}
            <div className="flex justify-between">
                <button
                    onClick={() => navigate(`/schedule-detail/${date}`)}
                    className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                >
                    ← 일정 현황으로 돌아가기
                </button>
                <button
                    onClick={handleSubmit}
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    일정 투표 등록
                </button>
            </div>
        </div>
    );
};

export default ScvWrite;
