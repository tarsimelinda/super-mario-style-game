import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage/HomePage";
import RegisterPage from "./components/RegisterPage/RegisterPage";
import GameCanvasWrapper from "./components/GameCanvasWrapper";
import Footer from "./components/Footer/Footer";

export default function App() {
  return (
    <BrowserRouter>
      <div className="appShell">
        <main className="appContent">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/register/:players" element={<RegisterPage />} />
            <Route path="/game" element={<GameCanvasWrapper />} />
          </Routes>
        </main>

        <Footer />
      </div>
    </BrowserRouter>
  );
}