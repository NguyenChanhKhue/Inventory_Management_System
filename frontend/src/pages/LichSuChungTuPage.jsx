import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import { useNavigate } from "react-router-dom";
import PaginationComponent from "../components/PaginationComponents";
import * as XLSX from "xlsx";

const LichSuChungTuComponent = () => {
  const [transactions, setTransactions] = useState([]);
  const [message, setMessage] = useState("");
  const [filter, setFilter] = useState("");
  const [valueToSearch, setValueToSearch] = useState("");
  const [filterType, setFilterType] = useState("MONTH");
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

  const navigate = useNavigate();

  //Pagination Set-Up
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    const getTransactions = async () => {
      try {
        const transactionData =
          await ApiService.getAllTransactions(valueToSearch);

        if (transactionData.status === 200) {
          let filteredList = transactionData.transactions || [];
          if (filterType === "DATE" && selectedDate) {
            filteredList = filteredList.filter(t => t.createAt && t.createAt.startsWith(selectedDate));
          } else if (filterType === "MONTH") {
            const monthStr = selectedMonth < 10 ? `0${selectedMonth}` : `${selectedMonth}`;
            const targetPrefix = `${selectedYear}-${monthStr}`;
            filteredList = filteredList.filter(t => t.createAt && t.createAt.startsWith(targetPrefix));
          }

          setTotalPages(
            Math.ceil(filteredList.length / itemsPerPage),
          );

          setTransactions(
            filteredList.slice(
              (currentPage - 1) * itemsPerPage,
              currentPage * itemsPerPage,
            ),
          );
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error Getting transactions: " + error,
        );
      }
    };

    getTransactions();
  }, [currentPage, valueToSearch, filterType, selectedDate, selectedMonth, selectedYear]);

  //Method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  //handle search
  const handleSearch = () => {
    setCurrentPage(1);
    setValueToSearch(filter);
  };

  const handleExportCSV = async () => {
    try {
      const transactionData = await ApiService.getAllTransactions(valueToSearch);
      let allTransactions = transactionData.transactions || [];
      if (filterType === "DATE" && selectedDate) {
        allTransactions = allTransactions.filter(t => t.createAt && t.createAt.startsWith(selectedDate));
      } else if (filterType === "MONTH") {
        const monthStr = selectedMonth < 10 ? `0${selectedMonth}` : `${selectedMonth}`;
        const targetPrefix = `${selectedYear}-${monthStr}`;
        allTransactions = allTransactions.filter(t => t.createAt && t.createAt.startsWith(targetPrefix));
      }

      if (allTransactions.length === 0) {
        showMessage("Không có dữ liệu để xuất");
        return;
      }
      
      const excelData = allTransactions.map(t => ({
        "Mã GD": t.id,
        "Loại giao dịch": { IMPORT: 'NHẬP KHO', EXPORT: 'XUẤT KHO', ADJUSTMENT: 'ĐIỀU CHỈNH', RETURN_TO_SUPPLIER: 'HOÀN TRẢ' }[t.transactionType] || t.transactionType || "",
        "Trạng thái": { COMPLETED: 'HOÀN THÀNH', CANCELLED: 'ĐÃ HỦY', PENDING: 'CHỜ XỬ LÝ', PROCESSING: 'ĐANG XỬ LÝ' }[t.transactionStatus] || t.transactionStatus || "",
        "Tổng tiền (VNĐ)": t.totalPrice || 0,
        "Tổng SP": t.totalProduct || 0,

        "Ngày tạo": new Date(t.createAt).toLocaleString(),
        "Mô tả": t.description || ""
      }));

      const worksheet = XLSX.utils.json_to_sheet(excelData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "Giao Dịch");
      
      XLSX.writeFile(workbook, "LichSuGiaoDich.xlsx");
    } catch (error) {
      showMessage("Lỗi xuất Excel: " + error);
    }
  };

  //Navigate to transactions details page
  const navigateToTransactionDetailsPage = (transactionId) => {
    navigate(`/lich-su-chung-tu/${transactionId}`);
  };

  return (
    <Layout>
      {message && <p className="message">{message}</p>}
      <div className="transactions-page">
        <div className="transactions-header">
          <h1>Lịch sử Giao dịch</h1>
          <div className="transaction-search" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', alignItems: 'center' }}>
            <input
              placeholder="Tìm kiếm giao dịch ..."
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
              type="text"
            />
            <button onClick={() => handleSearch()}>Tìm kiếm</button>

            <select 
              value={filterType} 
              onChange={(e) => { setFilterType(e.target.value); setCurrentPage(1); }}
              style={{ marginLeft: '10px', padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
            >
              <option value="MONTH">Lọc theo Tháng/Năm</option>
              <option value="DATE">Lọc theo Ngày</option>
            </select>

            {filterType === "MONTH" && (
              <>
                <select 
                  value={selectedMonth} 
                  onChange={(e) => { setSelectedMonth(parseInt(e.target.value, 10)); setCurrentPage(1); }} 
                  style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                >
                  {Array.from({ length: 12 }, (_, i) => <option key={i + 1} value={i + 1}>Tháng {i + 1}</option>)}
                </select>
                <select 
                  value={selectedYear} 
                  onChange={(e) => { setSelectedYear(parseInt(e.target.value, 10)); setCurrentPage(1); }} 
                  style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                >
                  {Array.from({ length: 5 }, (_, i) => {
                    const year = new Date().getFullYear() - i;
                    return <option key={year} value={year}>{year}</option>;
                  })}
                </select>
              </>
            )}

            {filterType === "DATE" && (
              <input 
                type="date" 
                value={selectedDate} 
                onChange={(e) => { setSelectedDate(e.target.value); setCurrentPage(1); }} 
                style={{ padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
              />
            )}

            <button
              onClick={handleExportCSV}
              style={{ backgroundColor: "#4CAF50", color: "white", marginLeft: "auto" }}
            >
              Xuất Excel (xlsx)
            </button>
          </div>
        </div>

        {transactions && (
          <table className="transactions-table">
            <thead>
              <tr>
                <th>Loại giao dịch</th>
                <th>Trạng thái</th>
                <th>Giá trị phiếu</th>
                <th>Tổng sản phẩm</th>
                <th>Thời gian</th>
                <th>Thao tác</th>
              </tr>
            </thead>

            <tbody>
              {transactions.map((transaction) => (
                <tr key={transaction.id}>
                  <td>{{ IMPORT: 'NHẬP KHO', EXPORT: 'XUẤT KHO', ADJUSTMENT: 'ĐIỀU CHỈNH', RETURN_TO_SUPPLIER: 'HOÀN TRẢ' }[transaction.transactionType] || transaction.transactionType}</td>
                  <td>{{ COMPLETED: 'HOÀN THÀNH', CANCELLED: 'ĐÃ HỦY', PENDING: 'CHỜ XỬ LÝ', PROCESSING: 'ĐANG XỬ LÝ' }[transaction.transactionStatus] || transaction.transactionStatus}</td>
                  <td>{transaction.totalPrice}</td>
                  <td>{transaction.totalProduct}</td>
                  <td>{new Date(transaction.createAt).toLocaleString()}</td>

                  <td>
                    <button
                      onClick={() =>
                        navigateToTransactionDetailsPage(transaction.id)
                      }
                    >
                      Chi tiết
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <PaginationComponent
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
    </Layout>
  );
};
export default LichSuChungTuComponent;
