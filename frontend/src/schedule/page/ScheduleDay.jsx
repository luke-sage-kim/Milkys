import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Header from "../../main/components/ui/Header";

const ScheduleDay = () => {
    const { date } = useParams();
    const navigate = useNavigate();
    const [scheduleVotes, setScheduleVotes] = useState([]);
    const [isVoted, setIsVoted] = useState(false);
    const [memberCode, setMemberCode] = useState(null);

    const hours = Array.from({ length: 16 }, (_, i) => i + 9); // [9, ..., 24]

    useEffect(() => {
        const code = sessionStorage.getItem('memberCode');
        setMemberCode(code);

        const fetchScheduleVotes = async () => {
            try {
                const response = await fetch(`http://localhost:8080/scv/v1/${date}`);
                const data = await response.json();
                if (data.status === 200) {
                    setScheduleVotes(data.resultData);
                    if (code && data.resultData.some(v => String(v.memberCode) === String(code))) {
                        setIsVoted(true);
                    }
                }
            } catch (error) {
                console.error('API 호출 오류:', error);
            }
        };

        fetchScheduleVotes();
    }, [date]);

    const memberGroups = {};
    scheduleVotes.forEach(vote => {
        if (!memberGroups[vote.memberNickName]) {
            memberGroups[vote.memberNickName] = [];
        }
        memberGroups[vote.memberNickName].push(vote);
    });

    const handleDeleteVote = async () => {
        if (!window.confirm("정말 삭제하시겠습니까?")) return;

        try {
            const response = await fetch('http://localhost:8080/scv/v1', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    scvDate: date,
                    memberCode: Number(memberCode),
                }),
            });


            if (response.ok) {
                alert("일정 투표가 삭제되었습니다.");
                setIsVoted(false); // 버튼 숨기기용
                setScheduleVotes(prev => prev.filter(v => String(v.memberCode) !== String(memberCode)));
            } else {
                alert("삭제에 실패했습니다.");
            }
        } catch (err) {
            console.error("삭제 요청 오류:", err);
            alert("삭제 중 오류가 발생했습니다.");
        }
    };


    return (
        <>
            <Header />
            <div className="pt-16 max-w-4xl mx-auto p-6 bg-white rounded-lg shadow">
                <h2 className="text-2xl font-bold text-center mb-6">{date} 일정 현황</h2>

                {/* 시간대 헤더 */}
                <div className="flex text-sm font-medium text-gray-700 border-b pb-2">
                    {hours.map(hour => (
                        <div key={hour} className="w-16 text-center">
                            {hour}시
                        </div>
                    ))}
                </div>

                {/* 멤버별 블록 */}
                <div className="space-y-4 mt-4">
                    {Object.entries(memberGroups).map(([nickName, votes], idx) => (
                        <div key={idx} className="relative h-8 w-full">
                            {votes.map((vote, i) => {
                                const startHour = parseInt(vote.scvStart.split(':')[0]);
                                const endHour = parseInt(vote.scvEnd.split(':')[0]);
                                const startMinute = parseInt(vote.scvStart.split(':')[1]);
                                const endMinute = parseInt(vote.scvEnd.split(':')[1]);

                                const totalHours = (endHour + (endMinute > 0 ? 1 : 0)) - startHour;
                                const leftPercent = ((startHour - 9) / 16) * 100;
                                const widthPercent = (totalHours / 16) * 100;

                                return (
                                    <div
                                        key={i}
                                        className="absolute h-full bg-blue-500 text-white text-sm flex items-center justify-center rounded"
                                        style={{
                                            left: `${leftPercent}%`,
                                            width: `${widthPercent}%`
                                        }}
                                    >
                                        {nickName}
                                    </div>
                                );
                            })}
                        </div>
                    ))}
                </div>

                {/* 버튼 */}
                <div className="flex justify-between mt-10">
                    <button
                        onClick={() => navigate('/schedule')}
                        className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
                    >
                        ← 달력으로 돌아가기
                    </button>

                    {/* 로그인 O & 아직 투표 안함 */}
                    {memberCode && !isVoted && (
                        <button
                            onClick={() => navigate(`/scv-write/${date}`)}
                            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                        >
                            일정 투표하기
                        </button>
                    )}

                    {/* 로그인 O & 이미 투표한 경우 → 수정/삭제 버튼 */}
                    {memberCode && isVoted && (
                        <div className="flex gap-2">
                            <button
                                onClick={() => {
                                    const myVote = scheduleVotes.find(v => String(v.memberCode) === String(memberCode));
                                    if (myVote) {
                                        navigate(`/scv-update/${date}/${myVote.id}`);
                                    } else {
                                        alert("수정할 데이터를 찾을 수 없습니다.");
                                    }
                                }}
                                className="px-4 py-2 bg-yellow-400 text-white rounded hover:bg-yellow-500"
                            >
                                일정 투표 수정
                            </button>
                            <button
                                onClick={handleDeleteVote}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                일정 투표 삭제
                            </button>
                        </div>
                    )}
                </div>

            </div>
        </>
    );
};

export default ScheduleDay;
