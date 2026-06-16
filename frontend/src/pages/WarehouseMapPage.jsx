import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { useLocation, useNavigate } from "react-router-dom";
import "../WarehouseMap.css"; // We will create this CSS file

const WarehouseMapPage = () => {
  const query = new URLSearchParams(useLocation().search);
  const highlightedLocation = query.get("location") || "";
  const navigate = useNavigate();

  const [activeZone, setActiveZone] = useState("");

  useEffect(() => {
    // Basic logic to determine which zone is highlighted based on the text
    const text = highlightedLocation.toUpperCase();
    if (text.includes("A")) setActiveZone("A");
    else if (text.includes("B")) setActiveZone("B");
    else if (text.includes("C")) setActiveZone("C");
    else if (text.includes("D")) setActiveZone("D");
    else if (text.includes("E")) setActiveZone("E");
    else if (text.includes("F")) setActiveZone("F");
    else if (text.includes("G")) setActiveZone("G");
    else if (text.includes("H")) setActiveZone("H");
  }, [highlightedLocation]);

  const renderZone = (zoneName) => {
    const isActive = activeZone === zoneName;
    return (
      <div className={`warehouse-zone ${isActive ? "active-zone" : ""}`}>
        <div className="zone-label">Khu {zoneName}</div>
        {isActive && <div className="blink-indicator">📍 Sản phẩm ở đây</div>}
      </div>
    );
  };

  return (
    <Layout>
      <div className="warehouse-map-container">
        <div className="warehouse-header">
          <h1>Sơ đồ Không gian Kho hàng</h1>
          <button className="back-btn" onClick={() => navigate(-1)}>
            &larr; Quay lại
          </button>
        </div>

        {highlightedLocation && (
          <div className="search-status">
            Đang tìm vị trí: <strong>{highlightedLocation}</strong>
          </div>
        )}

        <div className="warehouse-building">
          <div className="floor floor-2">
            <h2 className="floor-title">Tầng 2</h2>
            <div className="zones-grid">
              {renderZone("E")}
              {renderZone("F")}
              {renderZone("G")}
              {renderZone("H")}
            </div>
          </div>

          <div className="floor floor-1">
            <h2 className="floor-title">Tầng 1 (Trệt)</h2>
            <div className="zones-grid">
              {renderZone("A")}
              {renderZone("B")}
              {renderZone("C")}
              {renderZone("D")}
            </div>
            <div className="entrance">
              <span>CỬA CHÍNH (RA/VÀO)</span>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default WarehouseMapPage;
