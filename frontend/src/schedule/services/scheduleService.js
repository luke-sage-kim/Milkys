// schedule/services/scheduleService.js

export const getScheduleVotes = async (date) => {
    const response = await fetch(`http://localhost:8080/scv/v1/${date}`);
    const data = await response.json();

    if (data.status !== 200) {
        throw new Error('스케줄 조회 실패');
    }

    return data.resultData;
};

export const deleteScheduleVote = async (date, memberCode) => {
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

    if (!response.ok) {
        throw new Error('삭제 실패');
    }

    return true;
};

//  일정 삭제 API 추가
export const deleteSchedule = async (scDate) => {
    const response = await fetch(`http://localhost:8080/sc/v1/${scDate}`, {
        method: 'DELETE',
    });

    if (!response.ok) {
        throw new Error('일정 삭제 실패');
    }

    return true;
};