import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage/HomePage";
import RegisterPage from "./components/RegisterPage/RegisterPage";
import GameCanvasWrapper from "./components/GameCanvasWrapper";
import Footer from "./components/Footer";
import Maze from "./components/Maze/Maze";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Maze />} />
        <Route path="/register/:players" element={<RegisterPage />} />
        <Route path="/game" element={<GameCanvasWrapper />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}
