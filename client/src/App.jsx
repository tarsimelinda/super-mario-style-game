import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage/HomePage";
import RegisterPage from "./components/RegisterPage/RegisterPage";
import GameCanvasWrapper from "./components/GameCanvasWrapper";
import Footer from "./components/Footer/Footer";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/register/:players" element={<RegisterPage />} />
        <Route path="/game" element={<GameCanvasWrapper />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}
