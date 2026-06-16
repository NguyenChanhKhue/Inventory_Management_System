import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
} from "recharts";

const DashboardPage = () => {
  const [message, setMessage] = useState("");
  const [filterType, setFilterType] = useState("month"); 
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [selectedData, setSelectedData] = useState("amount");
  const [transactionData, setTransactionData] = useState([]);
  const [lowStockProducts, setLowStockProducts] = useState([]);
  const [summary, setSummary] = useState({ totalImportVouchers: 0, totalExportVouchers: 0, totalStockItems: 0, totalInventoryValue: 0 });

  useEffect(() => {
    const fetchData = async () => {
      try {
        let transactions = [];
        if (filterType === "month") {
          const res = await ApiService.geTransactionsByMonthAndYear(selectedMonth, selectedYear);
          if (res.status === 200) transactions = res.transactions || [];
        } else {
          // Lấy tất cả và lọc theo ngày ở frontend do backend hiện tại không có API lọc theo ngày riêng
          const res = await ApiService.getAllTransactions();
          if (res.status === 200) {
            transactions = (res.transactions || []).filter(t => t.createAt && t.createAt.startsWith(selectedDate));
          }
        }
        
        try {
          const summaryRes = await ApiService.getDashboardSummary();
          setSummary({
             totalImportVouchers: summaryRes.totalImportVouchers || 0,
             totalExportVouchers: summaryRes.totalExportVouchers || 0,
             totalStockItems: summaryRes.totalStockItems || 0,
             totalInventoryValue: summaryRes.totalInventoryValue || 0
          });
        } catch (e) {
          console.error("Error fetching summary", e);
        }
        setTransactionData(transformTransactionData(transactions));
      } catch (error) {
        showMessage(error.response?.data?.message || "Error fetching transactions");
      }
    };

    const fetchLowStock = async () => {
      try {
        const res = await ApiService.getLowStockProducts();
        if (res.status === 200) setLowStockProducts(res.products);
      } catch (error) {
        console.error("Error fetching low stock", error);
      }
    };

    fetchData();
    fetchLowStock();
  }, [filterType, selectedDate, selectedMonth, selectedYear]);



  const transformTransactionData = (transactions) => {
    const dataMap = {};
    const validTransactions = transactions.filter(t => t.transactionStatus === 'COMPLETED');

    if (filterType === "month") {
      const days = new Date(selectedYear, selectedMonth, 0).getDate();
      for (let day = 1; day <= days; day++) {
        dataMap[day] = { label: `Ngày ${day}`, importValue: 0, exportValue: 0 };
      }
      validTransactions.forEach((t) => {
        const day = new Date(t.createAt).getDate();
        if (dataMap[day]) {
          if (t.transactionType === 'IMPORT') {
            dataMap[day].importValue += t.totalPrice;
          } else if (t.transactionType === 'EXPORT' || t.transactionType === 'RETURN_TO_SUPPLIER') {
            dataMap[day].exportValue += t.totalPrice;
          }
        }
      });
    } else {
      // Nhóm theo giờ nếu chọn Ngày
      for (let hour = 0; hour < 24; hour++) {
        dataMap[hour] = { label: `${hour}:00`, importValue: 0, exportValue: 0 };
      }
      validTransactions.forEach((t) => {
        const hour = new Date(t.createAt).getHours();
        if (dataMap[hour]) {
          if (t.transactionType === 'IMPORT') {
            dataMap[hour].importValue += t.totalPrice;
          } else if (t.transactionType === 'EXPORT' || t.transactionType === 'RETURN_TO_SUPPLIER') {
            dataMap[hour].exportValue += t.totalPrice;
          }
        }
      });
    }
    return Object.values(dataMap);
  };

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => setMessage(""), 4000);
  };

  const formatCompact = (val) => new Intl.NumberFormat('en-US', { notation: 'compact', compactDisplay: 'short' }).format(val);

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="dashboard-page" style={{ padding: '20px' }}>
        
        {/* Filter Section */}
        <div style={{ display: 'flex', gap: '15px', marginBottom: '20px', alignItems: 'center' }}>
          <select value={filterType} onChange={(e) => setFilterType(e.target.value)} style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}>
            <option value="month">Lọc theo Tháng/Năm</option>
            <option value="date">Lọc theo Ngày</option>
          </select>
          {filterType === "month" ? (
            <>
              <select value={selectedMonth} onChange={(e) => setSelectedMonth(parseInt(e.target.value, 10))} style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}>
                {Array.from({ length: 12 }, (_, i) => <option key={i + 1} value={i + 1}>Tháng {i + 1}</option>)}
              </select>
              <select value={selectedYear} onChange={(e) => setSelectedYear(parseInt(e.target.value, 10))} style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}>
                {Array.from({ length: 5 }, (_, i) => {
                  const year = new Date().getFullYear() - i;
                  return <option key={year} value={year}>{year}</option>;
                })}
              </select>
            </>
          ) : (
            <input type="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }} />
          )}
        </div>

        {/* Summary Grid */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '15px', marginBottom: '20px' }}>
          {[{ title: "Số phiếu nhập", val: summary.totalImportVouchers }, { title: "Số phiếu xuất", val: summary.totalExportVouchers, subtitle: "(Bao gồm cả phiếu trả hàng NCC)" }, { title: "Tổng SP tồn kho", val: summary.totalStockItems }, { title: "Tổng giá trị tồn kho", val: summary.totalInventoryValue, curr: true }].map((item, idx) => (
            <div key={idx} style={{ padding: '20px', backgroundColor: 'white', borderRadius: '10px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)', textAlign: 'center' }}>
              <h4 style={{ margin: '0 0 5px', color: '#6b7280' }}>{item.title}</h4>
              {item.subtitle && <p style={{ fontSize: '0.8rem', color: '#9ca3af', margin: '0 0 10px 0' }}>{item.subtitle}</p>}
              <p style={{ margin: 0, fontSize: '1.5rem', fontWeight: 'bold', color: '#111827', marginTop: !item.subtitle ? '10px' : '0' }}>
                {item.curr ? formatCompact(item.val) + ' đ' : formatCompact(item.val)}
              </p>
            </div>
          ))}
        </div>

        {/* Low Stock Warning */}
        {lowStockProducts.length > 0 && (
          <div style={{ marginBottom: "20px", padding: "15px", backgroundColor: "#fff3cd", borderLeft: "5px solid #ff9800", borderRadius: "5px" }}>
            <h3 style={{ color: "#d32f2f", marginTop: 0 }}>⚠️ Cảnh báo Hàng sắp hết ({lowStockProducts.length})</h3>
            <table style={{ width: "100%", borderCollapse: "collapse", backgroundColor: "white" }}>
              <thead>
                <tr style={{ backgroundColor: "#ffc107", color: "black", textAlign: "left" }}>
                  <th style={{ padding: "8px", border: "1px solid #ddd" }}>Tên sản phẩm</th>
                  <th style={{ padding: "8px", border: "1px solid #ddd" }}>Tồn kho</th>
                  <th style={{ padding: "8px", border: "1px solid #ddd" }}>Tối thiểu</th>
                  <th style={{ padding: "8px", border: "1px solid #ddd" }}>Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                {lowStockProducts.map(p => (
                  <tr key={p.id}>
                    <td style={{ padding: "8px", border: "1px solid #ddd" }}>{p.name}</td>
                    <td style={{ padding: "8px", border: "1px solid #ddd", fontWeight: "bold", color: p.stockQuantity === 0 ? "red" : "orange" }}>{p.stockQuantity}</td>
                    <td style={{ padding: "8px", border: "1px solid #ddd" }}>{p.minStock}</td>
                    <td style={{ padding: "8px", border: "1px solid #ddd", color: p.stockQuantity === 0 ? "red" : "orange", fontWeight: "bold" }}>
                      {p.stockQuantity === 0 ? "Hết hàng" : "Sắp hết"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Chart */}
        <div className="chart-section" style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>
          <h3 style={{ marginBottom: "20px" }}>Biểu đồ Giá trị Nhập/Xuất</h3>
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={transactionData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="label" />
              <YAxis width={80} tickFormatter={(val) => formatCompact(val)} />
              <Tooltip formatter={(value) => new Intl.NumberFormat('vi-VN').format(value)} />
              <Legend />
              <Line name="Giá trị Nhập" type="monotone" dataKey="importValue" stroke="#4caf50" strokeWidth={2} activeDot={{ r: 8 }} />
              <Line name="Giá trị Xuất" type="monotone" dataKey="exportValue" stroke="#f44336" strokeWidth={2} activeDot={{ r: 8 }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </Layout>
  );
};

export default DashboardPage;
