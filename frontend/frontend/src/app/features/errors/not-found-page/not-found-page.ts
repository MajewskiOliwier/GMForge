import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {Footer} from '../../../shared/components/footer/footer';
import {Header} from '../../../shared/components/header/header';

@Component({
  selector: 'app-not-found-page',
  standalone: true,
  imports: [RouterLink, Footer],
  templateUrl: './not-found-page.html',
  styleUrl: './not-found-page.css'
})
export class NotFoundPage {}
