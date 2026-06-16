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
          setTotalPages(
            Math.ceil(transactionData.transactions.length / itemsPerPage),
          );

          setTransactions(
            transactionData.transactions.slice(
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
  }, [currentPage, valueToSearch]);

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
      const allTransactions = transactionData.transactions || [];
      if (allTransactions.length === 0) {
        showMessage("Không có dữ liệu để xuất");
        return;
      }
      
      const excelData = allTransactions.map(t => ({
        "Mã GD": t.id,
        "Loại giao dịch": t.transactionType || "",
        "Trạng thái": t.transactionStatus || "",
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
          <div className="transaction-search">
            <input
              placeholder="Tìm kiếm giao dịch ..."
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
              type="text"
            />
            <button onClick={() => handleSearch()}>Tìm kiếm</button>
            <button
              onClick={handleExportCSV}
              style={{ backgroundColor: "#4CAF50", color: "white", marginLeft: "10px" }}
            >
              Tải Excel (CSV)
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
