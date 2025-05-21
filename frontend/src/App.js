import React from 'react';
import { Routes, Route } from 'react-router-dom'; // BrowserRouter는 필요 없음
import './App.css';
import MainPage from './main/page/MainPage';
import LoginPage from './login/pages/LoginPage';
import SignUpPage from './member/pages/SignUpPage';
import Schedule from './schedule/page/Schedule';
import FindIdPage from "./login/pages/FindIdPage";
import FindPwPage from "./login/pages/FindPwPage";
import MyPage from "./member/pages/MyPage";
import UpdateMemberPage from "./member/pages/UpdateMemberPage";
import MusicMainPage from "./music/pages/MusicMainPage";
import MusicWritePage from "./music/pages/MusicWritePage";
import MusicDetailPage from "./music/pages/MusicDetailPage";
import MusicUpdatePage from "./music/pages/MusicUpdatePage";
import BoardMainPage from "./board/pages/BoardMainPage";
import BoardDetailPage from "./board/pages/BoardDetailPage";
import BoardWritePage from "./board/pages/BoardWritePage";
import BoardUpdatePage from "./board/pages/BoardUpdatePage";
import ScheduleDay from "./schedule/page/ScheduleDay";
import ScvWrite from "./schedule/page/ScheduleVote/ScvWrite";
import ScvUpdate from "./schedule/page/ScheduleVote/ScvUpdate";
import ScheduleWrite from "./schedule/page/ScheduleWrite";
import ScheduleUpdate from "./schedule/page/ScheduleUpdate";
import GalleryMainPage from "./gallery/pages/GalleryMainPage";
import GalleryWritePage from "./gallery/pages/GalleryWritePage";
import GalleryDetailPage from "./gallery/pages/GalleryDetailPage";
import GalleryUpdatePage from "./gallery/pages/GalleryUpdatePage";
import RecordingsMainPage from "./recordings/pages/RecordingsMainPage";
import RecordingDetailPage from "./recordings/pages/RecordingDetailPage";
import RecordingWritePage from "./recordings/pages/RecordingWritePage";
import AdminMainPage from "./admin/pages/AdminMainPage";
import AdminMemReqPage from "./admin/pages/AdminMemReqPage";
import AdminMemberPage from "./admin/pages/AdminMemberPage";


function App() {
  return (
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignUpPage />} />
          <Route path="/findId" element={<FindIdPage />} />
          <Route path="/findPw" element={<FindPwPage />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/update-member" element={<UpdateMemberPage />} />
          <Route path="/update-member" element={<UpdateMemberPage />} />
          <Route path="/music-main" element={<MusicMainPage />} />
          <Route path="/music-write" element={<MusicWritePage />} />
          <Route path="/music-detail/:id" element={<MusicDetailPage />} />
          <Route path="/music/update/:id" element={<MusicUpdatePage />} />

          <Route path="/board-main" element={<BoardMainPage />} />
          <Route path="/board-detail/:id" element={<BoardDetailPage />} />
          <Route path="/board-write" element={<BoardWritePage />} />
          <Route path="/board/update/:id" element={<BoardUpdatePage />} />


          <Route path="/gallery" element={<GalleryMainPage />} />
          <Route path="/gallery-write" element={<GalleryWritePage />} />
          <Route path="/gallery-detail/:id" element={<GalleryDetailPage />} />
          <Route path="/gallery/update/:id" element={<GalleryUpdatePage />} />


        <Route path="/recordings" element={<RecordingsMainPage />} />
          <Route path="/recording-detail/:id" element={<RecordingDetailPage />} />
          <Route path="/recording-write/:id" element={<RecordingWritePage />} />

        <Route path="/schedule" element={<Schedule />} />
          <Route path="/schedule-detail/:date" element={<ScheduleDay />} />
          <Route path="/scv-write/:date" element={<ScvWrite />} />
          <Route path="/scv-update/:date/:id" element={<ScvUpdate />} />

          <Route path="/schedule-write/:date" element={<ScheduleWrite />} />
          <Route path="/schedule-update/:date" element={<ScheduleUpdate />} />


          <Route path="/admin" element={<AdminMainPage />} />
          <Route path="/admin/member-approval" element={<AdminMemReqPage />} />
          <Route path="/admin/member-role" element={<AdminMemberPage />} />

      </Routes>
  );
}

export default App;
