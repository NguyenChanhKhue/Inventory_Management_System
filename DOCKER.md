# Docker Deployment

## 1. Tao file moi truong

Sao chep file mau:

```powershell
Copy-Item .env.docker.example .env
```

Cap nhat cac gia tri trong `.env`, dac biet:

- `SECRET_JWT_STRING`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`

## 2. Chay he thong

```powershell
docker compose up --build
```

Sau khi chay:

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:5050`

## 3. Dung he thong

```powershell
docker compose down
```

Neu muon xoa ca database va anh da upload:

```powershell
docker compose down -v
```

## Ghi chu

- Frontend duoc serve bang `nginx`.
- `nginx` proxy `/api` sang backend, nen frontend khong con hardcode `http://localhost:5050`.
- Anh san pham duoc luu trong volume Docker `product_images`, nen khong mat khi recreate container.
- Database duoc luu trong volume Docker `mysql_data`.
- MySQL chi mo trong network noi bo cua Docker Compose, khong bind ra host de tranh xung dot cong.
