import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-closable-panel',
  imports: [],
  templateUrl: './closable-panel.component.html',
  styleUrl: './closable-panel.component.css'
})
export class ClosablePanelComponent {
  @Input() buttonDisabled: boolean = false;

  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
