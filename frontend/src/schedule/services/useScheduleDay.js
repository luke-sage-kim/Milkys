import { useEffect, useState } from 'react';
import { getScheduleVotes, deleteScheduleVote } from './scheduleService';

export default function useScheduleDay(date) {
    const [scheduleVotes, setScheduleVotes] = useState([]);
    const [isVoted, setIsVoted] = useState(false);
    const [memberCode, setMemberCode] = useState(null);
    const [scheduleData, setScheduleData] = useState(null);  // 일정 정보 저장

    useEffect(() => {
        const code = sessionStorage.getItem('memberCode');
        setMemberCode(code);

        const fetchVotes = async () => {
            try {
                let votes = [];

                // 1차 시도: scv 데이터 호출
                const response1 = await fetch(`http://localhost:8080/sc/v1/${date}`);
                if (response1.ok) {
                    const data1 = await response1.json();
                    if (data1.status === 200 && Array.isArray(data1.resultData)) {
                        votes = data1.resultData;
                    }
                }

                // 2차 시도: 보조 API
                if (!votes || votes.length === 0) {
                    const response2 = await fetch(`http://localhost:8080/scv/v1/${date}`);
                    if (response2.ok) {
                        const data2 = await response2.json();
                        if (data2.status === 200 && Array.isArray(data2.resultData)) {
                            votes = data2.resultData;
                        }
                    }
                }

                setScheduleVotes(votes);

                if (code && votes.some(v => String(v.memberCode) === String(code))) {
                    setIsVoted(true);
                }

                // 일정 데이터 가져오기 (scv 데이터가 없으면 다른 방식으로 처리)
                if (!votes || votes.length === 0) {
                    const scheduleResponse = await fetch(`http://localhost:8080/sc/v1/${date}`);
                    if (scheduleResponse.ok) {
                        const scheduleData = await scheduleResponse.json();
                        if (scheduleData.status === 200 && scheduleData.resultData) {
                            setScheduleData(scheduleData.resultData);  // 일정 정보 설정
                        }
                    }
                }

            } catch (error) {
                console.error('API 호출 오류:', error);
            }
        };

        fetchVotes();
    }, [date]);

    const handleDeleteVote = async () => {
        if (!window.confirm("정말 삭제하시겠습니까?")) return;

        try {
            await deleteScheduleVote(date, memberCode);
            alert("삭제 완료");
            setIsVoted(false);
            setScheduleVotes(prev =>
                prev.filter(v => String(v.memberCode) !== String(memberCode))
            );
        } catch (error) {
            alert("삭제 실패");
        }
    };

    return {
        scheduleVotes,
        isVoted,
        memberCode,
        scheduleData,  // 일정 정보 추가
        handleDeleteVote,
    };
}
