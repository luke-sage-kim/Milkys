import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa"; // 마이페이지 아이콘
import { PiSignInBold, PiSignOutBold } from "react-icons/pi"; // 로그인 & 로그아웃 아이콘
import useSessionData from "../../../atom/components/MySession";

function Header() {
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();
    const [menuOpen, setMenuOpen] = useState(false);

    const handleLogout = async () => {
        try {
            const response = await fetch("http://localhost:8080/member/v1/logout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });

            if (response.ok) {
                sessionStorage.removeItem("memberId");
                sessionStorage.removeItem("memberNickname");
                navigate("/", { replace: true });
                window.location.reload();
            } else {
                console.error("로그아웃 실패");
            }
        } catch (error) {
            console.error("로그아웃 API 호출 중 오류 발생:", error);
        }
    };

    return (
        <>
            {/* 헤더 */}
            <header className="w-full fixed top-0 left-0 z-50 bg-gray-800 text-white shadow-md">
                <div className="flex justify-between items-center px-6 py-4">
                    {/* 로고 */}
                    <div
                        className="text-2xl font-bold cursor-pointer hover:text-gray-300 transition"
                        onClick={() => navigate("/")}
                    >
                        Milkys
                    </div>

                    {/* 햄버거 버튼 */}
                    <button
                        className="md:hidden text-2xl focus:outline-none"
                        onClick={() => setMenuOpen(!menuOpen)}
                    >
                        ☰
                    </button>

                    {/* 네비게이션 바 */}
                    <nav className="hidden md:flex gap-6">
                        <Link to="/music-main" className="hover:text-gray-400 transition">
                            Music
                        </Link>
                        <a href="#about" className="hover:text-gray-400 transition">
                            Recordings
                        </a>
                        <a href="#services" className="hover:text-gray-400 transition">
                            Gallery
                        </a>
                        <Link to="/board-main" className="hover:text-gray-400 transition">
                            Board
                        </Link>
                    </nav>

                    {/* 로그인/로그아웃 & 마이페이지 버튼 */}
                    <div className="hidden md:flex items-center gap-4">
                        {isLoggedIn ? (
                            <>
                                <span className="text-sm md:text-base">{userData.memberNickname}님</span>
                                <FaUserCircle
                                    className="text-2xl cursor-pointer hover:text-gray-300 transition"
                                    onClick={() => navigate("/mypage")}
                                    title="마이페이지"
                                />
                                <PiSignOutBold
                                    className="text-2xl cursor-pointer text-red-500 hover:text-red-400 transition"
                                    onClick={handleLogout}
                                    title="로그아웃"
                                />
                            </>
                        ) : (
                            <PiSignInBold
                                className="text-2xl cursor-pointer text-blue-500 hover:text-blue-400 transition"
                                onClick={() => navigate("/login")}
                                title="로그인"
                            />
                        )}
                    </div>
                </div>

                {/* 드롭다운 메뉴 */}
                {menuOpen && (
                    <div className="absolute top-full left-0 w-full bg-gray-900 text-center md:hidden flex flex-col py-4 shadow-lg">
                        <Link to="/music-main" className="py-2 text-white hover:bg-gray-700 transition" onClick={() => setMenuOpen(false)}>
                            Music
                        </Link>
                        <a href="#about" className="py-2 text-white hover:bg-gray-700 transition" onClick={() => setMenuOpen(false)}>
                            Recordings
                        </a>
                        <a href="#services" className="py-2 text-white hover:bg-gray-700 transition" onClick={() => setMenuOpen(false)}>
                            Gallery
                        </a>
                        <Link to="/board-main" className="py-2 text-white hover:bg-gray-700 transition" onClick={() => setMenuOpen(false)}>
                            Board
                        </Link>

                        <div className="border-t border-gray-600 my-2"></div>

                        {isLoggedIn ? (
                            <>
                                <span className="text-sm py-2 text-white">{userData.memberNickname}님</span>
                                <FaUserCircle className="text-3xl mx-auto cursor-pointer hover:text-gray-300 transition" onClick={() => navigate("/mypage")} title="마이페이지" />
                                <PiSignOutBold className="text-3xl mx-auto text-red-500 cursor-pointer hover:text-red-400 transition" onClick={handleLogout} title="로그아웃" />
                            </>
                        ) : (
                            <PiSignInBold className="text-3xl mx-auto text-blue-500 cursor-pointer hover:text-blue-400 transition" onClick={() => navigate("/login")} title="로그인" />
                        )}
                    </div>
                )}
            </header>

            {/* 헤더 높이만큼 패딩 추가해서 겹침 방지 */}
            <div className="pt-16"></div>
        </>
    );
}

export default Header;
