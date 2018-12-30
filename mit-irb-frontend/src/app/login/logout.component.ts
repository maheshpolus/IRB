import { Component} from '@angular/core';
import { Idle } from '@ng-idle/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html'
})
export class LogoutComponent {

   constructor(private idle: Idle, private _router: Router ) {
     this.idle.stop();
   }

   login() {
      this.reset();
      this._router.navigate(['/irb/dashboard']);
   }
   reset() {
       this.idle.watch();
     }

}
