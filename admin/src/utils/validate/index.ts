export const validDomain = (domain: string) => {
  const regex = new RegExp('(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]');
  if (regex.test(domain)) return true;
  return false;
};
