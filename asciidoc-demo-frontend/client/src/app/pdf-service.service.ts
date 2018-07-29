import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  API = 'http://localhost:8080';
  PDF_API = this.API + '/pdf-demo';

  constructor(private http: HttpClient) { }

  getPdf(input: string) {
    console.log("Will get " + this.PDF_API + '/' + input);
    return this.http.get('http://localhost:8080/pdf-demo/' + input, {responseType: 'blob'}).subscribe(
    response => {
      const blob = new Blob([response], {type: response.type});
      const url = window.URL.createObjectURL(blob);
      window.open(url);
    });
  }
}
