import Vue from 'vue';
import { ElMessageComponent, ElMessageOptions } from 'element-ui/types/message';

interface ElMessageMethods {
  (options: ElMessageOptions | string): ElMessageComponent
  success(msg: string | ElMessageOptions): ElMessageComponent
  warning(msg: string | ElMessageOptions): ElMessageComponent
  info(msg: string | ElMessageOptions): ElMessageComponent
  error(msg: string | ElMessageOptions): ElMessageComponent
  close(): void
}

declare module 'vue/types/vue' {
  interface Vue {
    $message: ElMessageMethods
    $confirm: any
    $alert: any
    $prompt: any
    $notify: any
    $loading: any
    $msgbox: any
  }
}
