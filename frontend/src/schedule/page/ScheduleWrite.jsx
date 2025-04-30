import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useSessionData from "../../atom/components/MySession";

const ScheduleWrite = () => {
    const { date } = useParams(); // scDate
    const { userData } = useSessionData();
    const navigate = useNavigate();

    const [scStart, setScStart] = useState('');
    const [scEnd, setScEnd] = useState('');
    const [scLoca, setScLoca] = useState('');
    const [scContent, setScContent] = useState('');

    // 09:00 ~ 24:00까지, 30분 단위 시간 목록 생성
    const generateTimeOptions = () => {
        const times = [];
        for (let h = 9; h <= 24; h++) {
            ['00', '30'].forEach(min => {
                if (h === 24 && min === '30') return; // 24:30은 제외
                const hour = String(h).padStart(2, '0');
                times.push(`${hour}:${min}`);
            });
        }
        return times;
    };

    const timeOptions = generateTimeOptions();

    const handleSubmit = async () => {
        if (!scStart || !scEnd || !scLoca || !scContent) {
            alert('모든 입력 필드를 채워주세요.');
            return;
        }

        // 시간 비교
        if (scStart >= scEnd) {
            alert('종료 시간은 시작 시간보다 늦어야 합니다.');
            return;
        }

        const payload = {
            scDate: date,
            scStart,
            scEnd,
            scLoca,
            scContent,
        };

        try {
            const response = await fetch('http://localhost:8080/sc/v1/write', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert('일정이 성공적으로 등록되었습니다!');
                navigate(`/schedule-detail/${date}`);
            } else {
                alert('일정 등록에 실패했습니다.');
            }
        } catch (error) {
            console.error('API 호출 오류:', error);
            alert('서버 오류가 발생했습니다.');
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow">
            <h2 className="text-2xl font-bold mb-6 text-center">{date} 일정 등록</h2>

            {/* 시작 시간 */}
            <div className="mb-4">
                <label className="block mb-2 font-medium">시작 시간</label>
                <select
                    value={scStart}
                    onChange={(e) => setScStart(e.target.value)}
                    className="w-full border px-3 py-2 rounded"
                >
                    <option value="">시간 선택</option>
                    {timeOptions.map(time => (
                        <option key={time} value={time}>{time}</option>
                    ))}
                </select>
            </div>

            {/* 종료 시간 */}
            <div className="mb-4">
                <label className="block mb-2 font-medium">종료 시간</label>
                <select
                    value={scEnd}
                    onChange={(e) => setScEnd(e.target.value)}
                    className="w-full border px-3 py-2 rounded"
                >
                    <option value="">시간 선택</option>
                    {timeOptions.map(time => (
                        <option key={time} value={time}>{time}</option>
                    ))}
                </select>
            </div>

            {/* 장소 */}
            <div className="mb-4">
                <label className="block mb-2 font-medium">장소 및 주소</label>
                <input
                    type="text"
                    value={scLoca}
                    onChange={(e) => setScLoca(e.target.value)}
                    className="w-full border px-3 py-2 rounded"
                    placeholder="예: 강남 연습실, 서울시 강남구 ..."
                />
            </div>

            {/* 내용 */}
            <div className="mb-6">
                <label className="block mb-2 font-medium">내용</label>
                <textarea
                    value={scContent}
                    onChange={(e) => setScContent(e.target.value)}
                    className="w-full border px-3 py-2 rounded h-32"
                    placeholder="일정에 대한 상세 내용을 작성해주세요"
                />
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
                    className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                >
                    일정 등록
                </button>
            </div>
        </div>
    );
};

export default ScheduleWrite;
