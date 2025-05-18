import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import Header from '../../main/components/ui/Header';
import useSessionData from '../../atom/components/MySession';
import CommentSection from "../../atom/components/CommentSection";

const RecordingDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { isLoggedIn, userData } = useSessionData();

    const [recordings, setRecordings] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedRecording, setSelectedRecording] = useState(null);
    const [audioUrl, setAudioUrl] = useState(null);
    const [audioError, setAudioError] = useState(false);

    useEffect(() => {
        const fetchRecordings = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/recording/v1/${id}`);
                const data = response.data?.resultData;

                if (Array.isArray(data) && data.length > 0) {
                    setRecordings(data);
                    setSelectedDate(data[0].recDate);
                    setSelectedRecording(data[0]);
                } else {
                    setRecordings([]);
                }
            } catch (error) {
                console.error('Error fetching recording details:', error);
                setRecordings([]);
            }
        };

        fetchRecordings();
    }, [id]);

    useEffect(() => {
        const fetchAudio = async () => {
            if (!selectedRecording) return;

            try {
                const res = await axios.post("http://localhost:8080/media/v1/list", {
                    domainType: "RECORDING",
                    parentId: selectedRecording.id,
                });

                const mediaList = res.data?.resultData;

                if (Array.isArray(mediaList) && mediaList.length > 0) {
                    const firstMedia = mediaList[0];
                    const fullPath = firstMedia.storedFilePath;
                    const fileName = fullPath.split('/').pop(); // 파일명만 추출
                    const encodedFileName = encodeURIComponent(fileName); // 인코딩
                    setAudioUrl(`http://localhost:8080/media/${encodedFileName}`);
                    setAudioError(false);
                }
                else {
                    setAudioUrl(null);
                    setAudioError(true);
                }
            } catch (err) {
                console.error("음원 가져오기 실패:", err);
                setAudioUrl(null);
                setAudioError(true);
            }
        };

        fetchAudio();
    }, [selectedRecording]);

    const handleDeleteClick = async () => {
        if (!selectedRecording) {
            alert("삭제할 음원을 선택해주세요.");
            return;
        }

        try {
            // DeleteRecordingDto 객체 생성
            const deleteDto = {
                recordingId: selectedRecording.id, // 선택된 음원의 id를 recordingId로 설정
            };

            // 음원 삭제 API 호출
            const response = await axios.delete("http://localhost:8080/recording/v1", {
                data: deleteDto, // 요청 본문에 deleteDto를 포함
            });

            // 응답이 성공적이면 날짜 목록에서 해당 음원 삭제
            if (response.status === 200) {
                setRecordings((prevRecordings) =>
                    prevRecordings.filter((rec) => rec.id !== selectedRecording.id)
                );
                setSelectedRecording(null); // 선택된 음원 초기화
                setAudioUrl(null); // 음원 URL 초기화
                setAudioError(true); // 음원 에러 처리
                alert("음원이 삭제되었습니다.");
            }
        } catch (error) {
            console.error("음원 삭제 실패:", error);
            alert("음원 삭제에 실패했습니다.");
        }
    };



    const handleWriteClick = () => {
        navigate(`/recording-write/${id}`);
    };

    const handleDateClick = (date) => {
        const recording = recordings.find(r => r.recDate === date);
        setSelectedDate(date);
        setSelectedRecording(recording);
    };

    const handleBackClick = () => {
        navigate('/recordings');
    };

    return (
        <div className="max-w-5xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />

            <div className="my-6">
                <h2 className="text-2xl font-bold">녹음 상세 정보</h2>
            </div>

            <div className="flex gap-6">
                {/* 상세정보 */}
                <div className="flex-1 border p-4 rounded shadow">
                    {selectedRecording ? (
                        <div>
                            <p><strong>제목:</strong> {selectedRecording.title}</p>
                            <p><strong>내용:</strong> {selectedRecording.content}</p>
                            <p><strong>날짜:</strong> {selectedRecording.recDate}</p>
                            <p><strong>작성자:</strong> {selectedRecording.memberNickName}</p>

                            {/* 음원 영역 */}
                            {/* 음원 영역 */}
                            <div className="mt-6 border-t pt-4">
                                <h4 className="font-semibold mb-2">🎵 음원</h4>

                                {audioError ? (
                                    <p className="text-sm text-gray-500">음원이 없습니다.</p>
                                ) : audioUrl ? (
                                    <audio controls src={audioUrl} className="w-full mb-2">
                                        Your browser does not support the audio element.
                                    </audio>
                                ) : (
                                    <p className="text-sm text-gray-400">로딩 중...</p>
                                )}

                                {/* 구분선 */}
                                <div className="border-t my-4"></div>

                                {/* ✅ 삭제 버튼 */}
                                {isLoggedIn && selectedRecording?.memberNickName === userData.memberNickname && (
                                    <button
                                        onClick={handleDeleteClick}
                                        className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition"
                                    >
                                        🗑 삭제
                                    </button>
                                )}
                            </div>

                        </div>
                    ) : (
                        <p>선택된 녹음이 없습니다.</p>
                    )}
                </div>

                {/* 날짜 목록 + 버튼들 */}
                <div className="w-48 border-l pl-4 flex flex-col justify-between">
                    <div>
                        <h3 className="font-semibold mb-2">날짜 목록</h3>
                        {recordings.length === 0 ? (
                            <p className="text-sm text-gray-500">등록된 음원이 없습니다.</p>
                        ) : (
                            <ul>
                                {recordings.map((rec) => (
                                    <li
                                        key={rec.id}
                                        className={`cursor-pointer mb-2 ${rec.recDate === selectedDate ? 'font-bold text-blue-600' : ''}`}
                                        onClick={() => handleDateClick(rec.recDate)}
                                    >
                                        {rec.recDate}
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>

                    <div className="mt-6 flex flex-col gap-2">
                        <button
                            onClick={handleBackClick}
                            className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 transition"
                        >
                            ← 목록으로 돌아가기
                        </button>

                        {isLoggedIn && (
                            <button
                                onClick={handleWriteClick}
                                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
                            >
                                + 음원 작성
                            </button>
                        )}
                    </div>
                </div>
            </div>

            {/* 댓글 영역 */}
            <CommentSection parentId={id} parentType="RECORDINGS" />
        </div>
    );

};

export default RecordingDetailPage;
