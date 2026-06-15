import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
import fs from 'fs'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    {
      name: 'serve-product-images',
      configureServer(server) {
        server.middlewares.use((req, res, next) => {
          if (req.url.startsWith('/products/')) {
            const fileName = req.url.substring('/products/'.length).split('?')[0];
            const filePath = path.resolve(__dirname, '../backend/product-images', fileName);
            if (fs.existsSync(filePath)) {
              const ext = path.extname(filePath).toLowerCase();
              let mime = 'application/octet-stream';
              if (ext === '.jpg' || ext === '.jpeg') mime = 'image/jpeg';
              else if (ext === '.png') mime = 'image/png';
              else if (ext === '.gif') mime = 'image/gif';
              res.setHeader('Content-Type', mime);
              fs.createReadStream(filePath).pipe(res);
              return;
            }
          }
          next();
        });
      }
    }
  ],
})

