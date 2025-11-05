import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('authToken');

  const publicUrls = ['/auth/login', '/auth/register'];
  const isPublicUrl = publicUrls.some((url) => req.url.includes(url));

  if (!isPublicUrl && token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req);
};
