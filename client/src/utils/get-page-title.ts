import defaultSettings from '@/settings';

const title = defaultSettings.title || '와로 샐러드';

export default function getPageTitle(pageTitle: string) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`;
  }
  return `${title}`;
}
