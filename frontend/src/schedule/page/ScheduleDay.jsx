import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Header from "../../main/components/ui/Header";
import useScheduleDay from '../services/useScheduleDay';
import TimeHeader from '../components/ui/TimeHeader';
import MemberTimelineRow from '../components/ui/MemberTimelineRow';
import {deleteSchedule} from "../services/scheduleService";

const ScheduleDay = () => {
    const { date } = useParams();
    const navigate = useNavigate();
    const { scheduleVotes, isVoted, memberCode, scheduleData, handleDeleteVote } = useScheduleDay(date);

    const hours = Array.from({ length: 16 }, (_, i) => i + 9);
    const memberGroups = {};

    if (Array.isArray(scheduleVotes)) {
        scheduleVotes.forEach(vote => {
            if (!memberGroups[vote.memberNickName]) {
                memberGroups[vote.memberNickName] = [];
            }
            memberGroups[vote.memberNickName].push(vote);
        });
    }

    return (
        <>
            <Header />
            <div className="pt-16 max-w-4xl mx-auto p-6 bg-white rounded-lg shadow">
                <h2 className="text-2xl font-bold text-center mb-6">{date} 일정 현황</h2>

                {/* 일정과 투표가 모두 없을 경우 */}
                {!scheduleData && (!Array.isArray(scheduleVotes) || scheduleVotes.length === 0) && (
                    <div className="text-center text-gray-500">
                        등록된 일정이 없습니다.
                    </div>
                )}

                {/* 일정 정보 표시 */}
                {scheduleData && (
                    <div className="bg-gray-100 p-4 rounded-lg mb-6">
                        <div><strong>시작 시간:</strong> {scheduleData.scStart}</div>
                        <div><strong>종료 시간:</strong> {scheduleData.scEnd}</div>
                        <div><strong>장소:</strong> {scheduleData.scLoca}</div>
                        <div><strong>내용:</strong> {scheduleData.scContent}</div>
                    </div>
                )}

                {/* 일정 없을 경우에만 투표 정보 표시 */}
                {!scheduleData && Array.isArray(scheduleVotes) && scheduleVotes.length > 0 && (
                    <>
                        <TimeHeader hours={hours} />
                        <div className="space-y-4 mt-4">
                            {Object.entries(memberGroups).map(([nick, votes], idx) => (
                                <MemberTimelineRow key={idx} nickName={nick} votes={votes} />
                            ))}
                        </div>
                    </>
                )}

                <div className="flex justify-between mt-10 flex-wrap gap-2">
                    <button
                        onClick={() => navigate('/schedule')}
                        className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
                    >
                        ← 달력으로 돌아가기
                    </button>

                    {/* 일정이 없고, 투표도 하지 않았을 때만 '일정 투표하기' 버튼 표시 */}
                    {memberCode && !isVoted && !scheduleData && (
                        <button
                            onClick={() => navigate(`/scv-write/${date}`)}
                            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                        >
                            일정 투표하기
                        </button>
                    )}

                    {/* 투표 했고, 일정이 없을 경우에만 수정/삭제 버튼 */}
                    {memberCode && isVoted && !scheduleData && (
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

                    {/* 일정이 존재할 경우 일정 수정/삭제 버튼 */}
                    {memberCode && scheduleData && (
                        <div className="flex gap-2">
                            <button
                                onClick={() => navigate(`/schedule-update/${date}`)}
                                className="px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600"
                            >
                                일정 수정
                            </button>
                            <button
                                onClick={async () => {
                                    if (window.confirm("일정을 삭제하시겠습니까?")) {
                                        try {
                                            await deleteSchedule(date);
                                            alert("일정이 삭제되었습니다.");
                                            navigate('/schedule'); // 삭제 후 리스트로 이동
                                        } catch (error) {
                                            alert("일정 삭제 실패");
                                            console.error(error);
                                        }
                                    }
                                }}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                일정 삭제
                            </button>
                        </div>
                    )}

                    {/* 로그인한 사용자에게 일정 등록 버튼은 항상 보임 */}
                    {memberCode && !scheduleData && (
                        <button
                            onClick={() => navigate(`/schedule-write/${date}`)}
                            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                        >
                            일정 등록
                        </button>
                    )}
                </div>
            </div>
        </>
    );
};

export default ScheduleDay;
