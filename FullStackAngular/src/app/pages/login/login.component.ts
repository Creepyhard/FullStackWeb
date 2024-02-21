import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  user: User = new User();
  hide = true;

  constructor(private userService: UserService, private router: Router) { }

  email = new FormControl('', [Validators.required, Validators.email]);

  getErrorMessageEmail() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  ngOnInit() {
  }

  Login() {
    this.userService.loginUserRemote(this.user).subscribe({
      error(err) {console.log("Error")},
      next: () => {console.log("Valid")},
      complete: () => this.router.navigate(["/home"])}
    )
  }
}
