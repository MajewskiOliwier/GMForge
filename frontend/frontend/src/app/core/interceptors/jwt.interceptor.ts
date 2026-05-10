import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

const PUBLIC_PATHS = ['/api/auth/login', '/api/auth/register'];

export const jwtInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const isPublic = PUBLIC_PATHS.some(path => req.url.includes(path));

  if (isPublic) {
    return next(req);
  }

  const token = localStorage.getItem('gmforge_token');

  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
};
