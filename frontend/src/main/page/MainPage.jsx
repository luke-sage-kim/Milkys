import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from "../components/ui/Header";

function MainPage() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [nickname, setNickname] = useState('');
    const [setList, setSetList] = useState([]);
    const [noticeList, setNoticeList] = useState([]); // 제목 + id


    const handleScheduleClick = () => {
        navigate('/schedule');
    };

    useEffect(() => {
        const memberId = sessionStorage.getItem('memberId');
        const memberNickname = sessionStorage.getItem('memberNickname');

        if (memberId) {
            setIsLoggedIn(true);
            setNickname(memberNickname);
        } else {
            setIsLoggedIn(false);
            setNickname('');
        }
    }, []);

    useEffect(() => {
        const fetchNotices = async () => {
            try {
                const response = await axios.get('http://localhost:8080/board/v1/notice');
                setNoticeList(Array.isArray(response.data.resultData) ? response.data.resultData : []);

            } catch (error) {
                console.error("공지사항 로딩 실패:", error);
            }
        };

        fetchNotices();
    }, []);


    // 셋리스트 불러오기
    useEffect(() => {
        const fetchSetList = async () => {
            try {
                const response = await axios.get('http://localhost:8080/music/v1/setList');
                setSetList(Array.isArray(response.data.resultData) ? response.data.resultData : []);

            } catch (error) {
                console.error("셋리스트 로딩 실패:", error);
            }
        };

        fetchSetList();
    }, []);


    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-100 p-4">
            <Header isLoggedIn={isLoggedIn} nickname={nickname} />

            {/* 공지사항 */}
            {noticeList.length > 0 ? (
                <div className="mt-6 w-full max-w-md bg-yellow-50 p-4 rounded-lg shadow-md border border-yellow-300">
                    <h3 className="text-xl font-semibold mb-2 text-center text-yellow-800">📢 공지사항</h3>
                    <ul className="list-disc list-inside text-gray-700 space-y-1">
                        {noticeList.map((notice) => (
                            <li
                                key={notice.id}
                                onClick={() => navigate(`/board-detail/${notice.id}`)}
                                className="cursor-pointer hover:text-yellow-700 transition-colors"
                            >
                                {notice.title}
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <div className="mt-6 w-full max-w-md bg-yellow-50 p-4 rounded-lg shadow-md border border-yellow-300 text-center text-yellow-800">
                    📢 공지사항이 없습니다.
                </div>
            )}

            {/* 셋리스트 */}
            {setList.length > 0 ? (
                <div className="mt-10 w-full max-w-md bg-white p-4 rounded-lg shadow-md">
                    <h3 className="text-xl font-semibold mb-2 text-center">🎵 SetList</h3>
                    <ul className="list-disc list-inside text-gray-700 space-y-1">
                        {setList.map((music) => (
                            <li
                                key={music.id}
                                onClick={() => navigate(`/music-detail/${music.id}`)}
                                className="cursor-pointer hover:text-blue-600 transition-colors"
                            >
                                {music.title}
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <div className="mt-10 w-full max-w-md bg-white p-4 rounded-lg shadow-md text-center text-gray-500">
                    🎵 셋리스트가 없습니다.
                </div>
            )}
        </div>
    );

}

export default MainPage;
