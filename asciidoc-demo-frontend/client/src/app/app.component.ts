import { PdfService } from './pdf-service.service';
import { Component, Input } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PDF with AsciiDoc, Jtwig and FlyingSaucer';
  
  @Input() input: string; 

  constructor(private pdfService: PdfService) { }
  
  getPdf(input: String) {
    console.log("I've been clicked! Input is -> " + this.input);
    this.pdfService.getPdf(this.input);
  }
}
