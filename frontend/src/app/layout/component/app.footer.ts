import { Component } from '@angular/core';

@Component({
    standalone: true,
    selector: 'app-footer',
    template: `<div class="layout-footer">
        MovieRama by <a href="https://www.linkedin.com/in/manos-kavakakis/" target="_blank" rel="noopener noreferrer" class="text-primary font-bold hover:underline">Me</a>
    </div>`
})
export class AppFooter {}
