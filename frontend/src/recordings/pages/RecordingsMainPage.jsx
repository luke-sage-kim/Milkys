import React, { useEffect, useState } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import { useNavigate } from "react-router-dom";

const RecordingsMainPage = () => {
    const [recordings, setRecordings] = useState([]);
    const [loading, setLoading] = useState(true);
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRecordings = async () => {
            try {
                const response = await axios.get("http://localhost:8080/music/v1/setList");
                setRecordings(Array.isArray(response.data.resultData) ? response.data.resultData : []);
            } catch (error) {
                console.error("셋리스트를 가져오는 중 오류 발생:", error);
                setRecordings([]);  // 오류 시 빈 배열 세팅
            } finally {
                setLoading(false);
            }
        };

        fetchRecordings();
    }, []);


    const colors = ['#FFD700', '#87CEEB', '#FFB6C1', '#98FB98', '#FFA07A'];

    const handleClick = (record) => {
        navigate(`/recording-detail/${record.id}`);
    };

    if (loading) {
        return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">합주 기록보관소</h1>

            <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3">
                {recordings.map((record, idx) => (
                    <div
                        key={record.id}
                        onClick={() => handleClick(record)}
                        className="w-48 h-64 bg-white rounded-xl shadow-lg border-2 border-gray-200 relative cursor-pointer transition-transform hover:scale-105"
                    >
                        {/* 타이틀 박스 */}
                        <div className="bg-gray-800 text-white text-center py-2 rounded-t-xl">
                            <span className="text-sm font-semibold">{record.title}</span>
                        </div>

                        {/* 앨범 중앙 아이콘 */}
                        <div className="flex items-center justify-center h-full px-4">
                            <div className="w-20 h-20 bg-gray-300 rounded-full flex items-center justify-center">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    strokeWidth={1.5}
                                    stroke="black"
                                    className="w-8 h-8"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        d="M9 19V6l12-2v13M9 19a2 2 0 100-4 2 2 0 000 4zm12-3a2 2 0 11-4 0 2 2 0 014 0z"
                                    />
                                </svg>
                            </div>
                        </div>
                    </div>
                ))}
            </div>


        </div>
    );
};

export default RecordingsMainPage;
